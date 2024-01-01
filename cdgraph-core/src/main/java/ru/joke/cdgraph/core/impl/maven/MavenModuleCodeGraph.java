package ru.joke.cdgraph.core.impl.maven;

import org.apache.maven.api.model.Dependency;
import org.apache.maven.api.model.DependencyManagement;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Parent;
import org.apache.maven.model.v4.MavenStaxReader;
import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.AbstractCodeGraph;
import ru.joke.cdgraph.core.impl.SimpleGraphNode;
import ru.joke.cdgraph.core.impl.SimpleGraphNodeRelation;
import ru.joke.cdgraph.core.impl.SimpleGraphTag;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

// TODO refactoring, optimizations and tests, its draft impl
@NotThreadSafe
public final class MavenModuleCodeGraph extends AbstractCodeGraph {

    private static final String MAVEN_REPO_BASE_URL = "https://repo1.maven.org/maven2/";
    private static final String POM_XML = "/pom.xml";

    private static final String MODULE_NAME_TAG = "name";
    private static final String MODULE_DESCRIPTION_TAG = "description";
    private static final String MODULE_GROUP_TAG = "group";
    private static final String MODULE_PACKAGING_TAG = "packaging";

    private static final String CLASSIFIER_TAG = "classifier";
    private static final String DEPENDENCY_TYPE_TAG = "dependency-type";
    private static final String OPTIONAL_TAG = "optional";
    private static final String SYSTEM_PATH_TAG = "system-path";

    private static final String TEST_SCOPE = "test";

    private final MavenStaxReader modelReader;
    private final ClassesMetadataReader classesMetadataReader;
    private final Map<String, Model> modelsById = new HashMap<>();

    public MavenModuleCodeGraph(
            @Nonnull CodeGraphDataSource dataSource,
            @Nonnull ClassesMetadataReader classesMetadataReader) {
        super(dataSource);
        this.classesMetadataReader = classesMetadataReader;
        this.modelReader = new MavenStaxReader();
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource) {

        final var configs = dataSource.find(fileName -> fileName.endsWith(POM_XML));
        final var configsMap =
                configs.stream()
                        .map(this::readOneConfig)
                        .map(Map::entrySet)
                        .flatMap(Set::stream)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));
        final Map<String, Model> modelsMap = new HashMap<>();
        final Map<String, GraphNode> nodesMap = new HashMap<>();
        configsMap.forEach((moduleNode, model) -> {
            nodesMap.put(moduleNode.id(), moduleNode);
            modelsMap.put(moduleNode.id(), model);
        });

        try (final HttpClient client = createHttpClient()) {
            final Set<String> processedModules = new HashSet<>();
            configsMap.forEach((moduleNode, model) -> processModuleDependencies(client, model, moduleNode, nodesMap, modelsMap, Collections.emptySet(), processedModules));

            return nodesMap;
        }
    }

    private void processModuleDependencies(
            final HttpClient client,
            final Model moduleModel,
            final GraphNode moduleNode,
            final Map<String, GraphNode> modulesMap,
            final Map<String, Model> modelsMap,
            final Set<String> exclusions,
            final Set<String> processedModules) {

        final var moduleId = createModuleId(moduleModel);
        if (!processedModules.add(moduleId)) {
            return;
        }

        final Set<Dependency> dependencies = new LinkedHashSet<>();

        final Map<String, Dependency> dependencyManagementMap = new HashMap<>();
        collectDependenciesFromDependencyManagement(dependencyManagementMap, moduleModel.getDependencyManagement());

        collectParentDependencies(client, moduleModel, dependencies, dependencyManagementMap);
        dependencies.addAll(moduleModel.getDependencies());

        dependencies
                .stream()
                .filter(dependency -> !TEST_SCOPE.equals(dependency.getScope()))
                .filter(dependency -> !exclusions.contains(composeUnversionedDependencyArtifactId(dependency.getGroupId(), dependency.getArtifactId())))
                .map(dependency -> getVersionedDependency(dependency, dependencyManagementMap))
                .forEach(dependency -> {

                    final String artifactFullId = createModuleId(dependency);
                    final String artifactUri = composeArtifactModelUri(dependency);

                    final GraphNodeRelation.RelationType relationType =
                            dependency.getScope() == null
                                    ? GraphNodeRelation.RelationType.COMPILE
                                    : GraphNodeRelation.RelationType.valueOf(dependency.getScope().toUpperCase());
                    final Model dependencyModel = modelsMap.computeIfAbsent(artifactFullId, id -> readModuleModel(client, artifactUri));

                    final GraphNode dependencyModuleNode = modulesMap.computeIfAbsent(artifactFullId, id -> new SimpleGraphNode(id, new HashSet<>(), createModuleTags(dependencyModel, readClassesMetadata(client, dependency))));
                    moduleNode.relations().add(new SimpleGraphNodeRelation(moduleNode, dependencyModuleNode, relationType, createDependencyTags(dependency)));

                    final Set<String> currentDependencyExclusions = new HashSet<>(exclusions);
                    dependency.getExclusions().stream().map(exclusion -> composeUnversionedDependencyArtifactId(exclusion.getGroupId(), exclusion.getArtifactId()))
                            .forEach(currentDependencyExclusions::add);
                    if (relationType != GraphNodeRelation.RelationType.PROVIDED) {
                        processModuleDependencies(client, dependencyModel, dependencyModuleNode, modulesMap, modelsMap, currentDependencyExclusions, processedModules);
                    }
                });
    }

    private Dependency getVersionedDependency(final Dependency dependency, final Map<String, Dependency> dependencyManagementMap) {

        if (dependency.getVersion() != null) {
            return dependency;
        }

        final var unversionedArtifactId = composeUnversionedDependencyArtifactId(dependency.getGroupId(), dependency.getArtifactId());
        final var artifactVersionFromDependenciesManagement = dependencyManagementMap.get(unversionedArtifactId);

        if (artifactVersionFromDependenciesManagement != null) {
            return dependency.withVersion(artifactVersionFromDependenciesManagement.getVersion());
        }

        throw new CodeGraphConfigurationException("Unable to find dependency version");
    }

    private Set<ClassMetadata> readClassesMetadata(final HttpClient client, final Dependency dependency) {
        final String uri = composeArtifactJarUri(dependency);

        File jarFile = null;
        try {
            jarFile = File.createTempFile(createModuleId(dependency), ".jar");

            try (final InputStream dependencyStream = executeRequest(client, uri);
                    final FileOutputStream fos = new FileOutputStream(jarFile)) {
                fos.write(dependencyStream.readAllBytes());
            } catch (IOException | InterruptedException e) {
                throw new CodeGraphDataSourceException(e);
            }

            return this.classesMetadataReader.read(new JarFile(jarFile));
        } catch (IOException ex) {
            throw new CodeGraphDataSourceException(ex);
        } finally {
            if (jarFile != null) {
                jarFile.delete();
            }
        }
    }

    private HttpClient createHttpClient() {
        return HttpClient
                .newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    private Map<GraphNode, Model> readOneConfig(final CodeGraphDataSource.Configuration configuration) {
        try (final var is = new FileInputStream(configuration.descriptor())) {
            final var model = this.modelReader.read(is);

            final var node = createModuleNode(model, configuration.classesMetadata());
            return Map.of(node, model);
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void collectParentDependencies(
            final HttpClient client,
            final Model model,
            final Set<Dependency> dependencies,
            final Map<String, Dependency> dependencyManagementMap) {

        var parent = model.getParent();
        while (parent != null) {
            final var parentModel = readParentModuleModel(client, parent);

            final var dependencyManagement = parentModel.getDependencyManagement();
            collectDependenciesFromDependencyManagement(dependencyManagementMap, dependencyManagement);

            final var parentDependencies = parentModel.getDependencies();
            dependencies.addAll(parentDependencies);

            parent = parentModel.getParent();
        }
    }

    private void collectDependenciesFromDependencyManagement(
            final Map<String, Dependency> dependencyManagementMap,
            final DependencyManagement dependencyManagement) {
        if (dependencyManagement == null) {
            return;
        }

        dependencyManagement.getDependencies().forEach(dependency -> {
            final var unversionedArtifactId = composeUnversionedDependencyArtifactId(dependency.getGroupId(), dependency.getArtifactId());
            dependencyManagementMap.putIfAbsent(unversionedArtifactId, dependency);
        });
    }

    private GraphNode createModuleNode(final Model model, final Set<ClassMetadata> metadata) {
        final var moduleId = createModuleId(model);
        return new SimpleGraphNode(moduleId, new HashSet<>(), createModuleTags(model, metadata));
    }

    private Map<String, GraphTag<?>> createModuleTags(final Model model, final Set<ClassMetadata> metadata) {
        final Set<GraphTag<?>> tags = new HashSet<>(7);
        tags.add(new SimpleGraphTag<>(VERSION_TAG, model.getVersion()));
        tags.add(new SimpleGraphTag<>(SOURCE_MODULE_TAG, true));

        if (model.getName() != null) {
            tags.add(new SimpleGraphTag<>(MODULE_NAME_TAG, model.getName()));
        }

        if (model.getDescription() != null) {
            tags.add(new SimpleGraphTag<>(MODULE_DESCRIPTION_TAG, model.getDescription()));
        }

        tags.add(new SimpleGraphTag<>(MODULE_GROUP_TAG, model.getGroupId()));

        if (model.getPackaging() != null) {
            tags.add(new SimpleGraphTag<>(MODULE_PACKAGING_TAG, model.getPackaging()));
        }

        final var classesMetadataTags = collectModuleClassesMetadataTags(metadata);
        tags.addAll(classesMetadataTags.values());

        return tags
                .stream()
                .collect(Collectors.toMap(GraphTag::name, Function.identity()));
    }

    private Map<String, GraphTag<?>> createDependencyTags(final Dependency dependency) {
        final Set<GraphTag<?>> tags = new HashSet<>(4);

        if (dependency.getClassifier() != null) {
            tags.add(new SimpleGraphTag<>(CLASSIFIER_TAG, dependency.getClassifier()));
        }

        if (dependency.getType() != null) {
            tags.add(new SimpleGraphTag<>(DEPENDENCY_TYPE_TAG, dependency.getType()));
        }

        if (dependency.isOptional()) {
            tags.add(new SimpleGraphTag<>(OPTIONAL_TAG, true));
        }

        if (dependency.getSystemPath() != null) {
            tags.add(new SimpleGraphTag<>(SYSTEM_PATH_TAG, dependency.getSystemPath()));
        }

        return tags
                .stream()
                .collect(Collectors.toMap(GraphTag::name, Function.identity()));
    }

    private String createModuleId(final Model model) {
        final String groupId = model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId();
        final String version = model.getVersion() == null ? model.getParent().getVersion() : model.getVersion();
        return createModuleId(groupId, model.getArtifactId(), version);
    }

    private String createModuleId(final Dependency dependency) {
        return createModuleId(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion());
    }

    private String createModuleId(final String groupId, final String artifactId, final String version) {
        return groupId + ":" + artifactId + ":" + version;
    }

    private Model readParentModuleModel(final HttpClient client, final Parent parent) {
        final var uri = composeArtifactModelUri(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
        return readModuleModel(client, uri);
    }

    private Model readModuleModel(final HttpClient client, final String uri) {
        try (final var modelStream = executeRequest(client, uri)) {
            return modelReader.read(modelStream);
        } catch (XMLStreamException | IOException | InterruptedException e) {
            throw new CodeGraphDataSourceException(e);
        }
    }

    private InputStream executeRequest(final HttpClient client, final String uri) throws IOException, InterruptedException {
        final URI url = URI.create(MAVEN_REPO_BASE_URL + uri);
        final var request = HttpRequest
                                .newBuilder()
                                    .GET()
                                    .uri(url)
                                    .timeout(Duration.ofSeconds(10))
                                .build();
        final var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() != 200) {
            throw new CodeGraphDataSourceException("Unable to request artifact descriptor from repo: response status code is " + response.statusCode());
        }

        return response.body();
    }

    private String composeUnversionedDependencyArtifactId(final String groupId, final String artifactId) {
        return groupId + ":" + artifactId;
    }

    private String composeArtifactJarUri(final Dependency dependency) {
        return composeArtifactUri(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), "jar");
    }

    private String composeArtifactModelUri(final Dependency dependency) {
        return composeArtifactUri(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), "pom");
    }

    private String composeArtifactModelUri(final String groupId, final String artifactId, final String version) {
        return composeArtifactUri(groupId, artifactId, version, "pom");
    }

    private String composeArtifactUri(
            final String groupId,
            final String artifactId,
            final String version,
            final String dataType) {
        return groupId.replace(".", "/")
                + "/"
                + artifactId
                + "/"
                + version
                + "/"
                + (artifactId + "-" + version + "." + dataType);
    }
}
