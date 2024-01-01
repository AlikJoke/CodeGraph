package ru.joke.cdgraph.core.impl.maven;

import org.apache.maven.api.model.Model;
import org.apache.maven.model.v4.MavenStaxReader;
import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.AbstractCodeGraph;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@NotThreadSafe
public final class MavenModuleCodeGraph extends AbstractCodeGraph<MavenModuleCodeGraph.Context> {

    public static final String CLASSIFIER_TAG = "classifier";
    public static final String DEPENDENCY_TYPE_TAG = "dependency-type";
    public static final String OPTIONAL_TAG = "optional";
    public static final String SYSTEM_PATH_TAG = "system-path";

    public static final String MODULE_NAME_TAG = "name";
    public static final String MODULE_DESCRIPTION_TAG = "description";
    public static final String MODULE_GROUP_TAG = "group";
    public static final String MODULE_PACKAGING_TAG = "packaging";

    public static final String POM = "pom";
    public static final String POM_XML = "/" + POM + ".xml";
    public static final String ID_PART_DELIMITER = ":";

    public MavenModuleCodeGraph(
            @Nonnull CodeGraphDataSource dataSource,
            @Nonnull ClassesMetadataReader classesMetadataReader,
            @Nonnull String mavenRepositoryBaseUrl) {
        super(dataSource, new Context(classesMetadataReader, mavenRepositoryBaseUrl, new MavenStaxReader(), new MavenGraphNodeBuilder()));
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(
            @Nonnull CodeGraphDataSource dataSource,
            @Nonnull MavenModuleCodeGraph.Context context) {

        final var configs = dataSource.find(fileName -> fileName.endsWith(POM_XML));

        final Set<Pair<GraphNode, Model>> configsPairs = new LinkedHashSet<>(configs.size(), 1);
        final Map<String, Pair<GraphNode, Model>> modulesMap = new HashMap<>();

        configs.stream()
                .map(config -> readOneConfig(config, context))
                .forEach(configPair -> {
                    final GraphNode moduleNode = configPair.first();

                    configsPairs.add(configPair);
                    modulesMap.put(moduleNode.id(), configPair);
                });

        try (final HttpClient client = createHttpClient()) {
            final Set<String> processedModules = new HashSet<>();
            final var remoteMavenModuleReader = new RemoteMavenModuleReader(client, context.mavenRepositoryBaseUrl, context.classesMetadataReader, context.modelReader);

            configsPairs.forEach(moduleData -> {
                final var dependenciesReader = new MavenModuleDependenciesReader(remoteMavenModuleReader, modulesMap, processedModules, context.graphNodeBuilder);
                dependenciesReader.read(moduleData, Collections.emptySet());
            });

            return modulesMap.entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().first()));
        }
    }

    private HttpClient createHttpClient() {
        return HttpClient
                .newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    private Pair<GraphNode, Model> readOneConfig(final CodeGraphDataSource.Configuration configuration, final Context context) {
        try (final var is = new FileInputStream(configuration.descriptor())) {
            final var model = context.modelReader.read(is);
            final var moduleId = createModuleId(model);

            final var node = context.graphNodeBuilder.build(moduleId, model, configuration.classesMetadata());
            return new Pair<>(node, model);
        } catch (IOException | XMLStreamException e) {
            throw new CodeGraphConfigurationException(e);
        }
    }

    private String createModuleId(final Model model) {
        final String groupId = model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId();
        final String version = model.getVersion() == null ? model.getParent().getVersion() : model.getVersion();
        return createModuleId(groupId, model.getArtifactId(), version);
    }

    private String createModuleId(final String groupId, final String artifactId, final String version) {
        return groupId + ID_PART_DELIMITER + artifactId + ID_PART_DELIMITER + version;
    }

    public record Context(
            @Nonnull ClassesMetadataReader classesMetadataReader,
            @Nonnull String mavenRepositoryBaseUrl,
            @Nonnull MavenStaxReader modelReader,
            @Nonnull MavenGraphNodeBuilder graphNodeBuilder) implements AbstractCodeGraph.Context {
    }
}
