package ru.joke.cdgraph.core.impl.maven;

import org.apache.maven.api.model.Model;
import org.apache.maven.model.v4.MavenStaxReader;
import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphConfigurationException;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.AbstractCodeGraph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.*;
import java.util.jar.JarFile;
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
    public static final String POM_XML = POM + ".xml";
    public static final String ID_PART_DELIMITER = ":";

    static final String WAR_TYPE = "war";
    static final String EAR_TYPE = "ear";

    public MavenModuleCodeGraph(
            @Nonnull CodeGraphDataSource dataSource,
            @Nonnull ClassesMetadataReader<JarFile> classesMetadataReader,
            @Nonnull String mavenRepositoryBaseUrl,
            @Nullable PasswordAuthentication mavenRepositoryCredentials) {
        super(dataSource, Context.create(classesMetadataReader, mavenRepositoryBaseUrl, mavenRepositoryCredentials));
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(
            @Nonnull CodeGraphDataSource dataSource,
            @Nonnull MavenModuleCodeGraph.Context context) {

        final var configs = dataSource.find(fileName -> fileName.endsWith(POM_XML));

        final Set<Pair<GraphNode, Model>> configsPairs = new LinkedHashSet<>(configs.size(), 1);
        final Map<String, Pair<GraphNode, Model>> modulesMap = new HashMap<>();
        final Map<String, Model> parentModels = new HashMap<>();

        configs.stream()
                .map(config -> readOneConfig(config, context))
                .sorted(createConfigComparator())
                .forEach(configPair -> {
                    final GraphNode moduleNode = configPair.first();
                    final Model moduleModel = configPair.second();

                    if (POM.equals(moduleModel.getPackaging())) {
                        parentModels.put(moduleNode.id(), moduleModel);
                    } else {
                        configsPairs.add(configPair);
                        modulesMap.put(moduleNode.id(), configPair);
                    }
                });

        try (final HttpClient client = createHttpClient(context)) {
            final Set<String> processedModules = new HashSet<>();
            final var remoteMavenModuleReader = createRemoteMavenModuleReader(client, context);

            configsPairs.forEach(moduleData -> {
                final var dependenciesReader =
                        MavenModuleDependenciesReader
                                .builder()
                                        .withGraphNodeBuilder(context.graphNodeBuilder())
                                        .withModules(modulesMap)
                                        .withAlreadyProcessedModules(processedModules)
                                        .withParentModels(parentModels)
                                        .withRemoteMavenModuleReader(remoteMavenModuleReader)
                                .build();
                dependenciesReader.read(moduleData);
            });

            return modulesMap.entrySet()
                                .stream()
                                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().first()));
        }
    }

    private RemoteMavenModuleReader createRemoteMavenModuleReader(@Nonnull HttpClient client, @Nonnull Context context) {
        return RemoteMavenModuleReader.builder()
                                        .withHttpClient(client)
                                        .withMavenModelReader(context.modelReader())
                                        .withMavenRepositoryBaseUrl(context.mavenRepositoryBaseUrl)
                                        .withClassesMetadataReader(context.classesMetadataReader)
                                      .build();
    }

    private Comparator<Pair<GraphNode, Model>> createConfigComparator() {
        return Comparator.comparingInt(
                pair -> WAR_TYPE.equalsIgnoreCase(pair.second().getPackaging())
                        || EAR_TYPE.equalsIgnoreCase(pair.second().getPackaging()) ? 0 : 1
        );
    }

    private HttpClient createHttpClient(@Nonnull Context context) {
        return HttpClient
                .newBuilder()
                    .connectTimeout(Duration.ofSeconds(5))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .authenticator(new Authenticator() {
                        @Override
                        public PasswordAuthentication getPasswordAuthentication() {
                            return context.mavenRepositoryCredentials();
                        }
                    })
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
            @Nonnull ClassesMetadataReader<JarFile> classesMetadataReader,
            @Nonnull String mavenRepositoryBaseUrl,
            @Nonnull MavenStaxReader modelReader,
            @Nonnull MavenGraphNodeBuilder graphNodeBuilder,
            @Nullable PasswordAuthentication mavenRepositoryCredentials) implements AbstractCodeGraph.Context {

        @Nonnull
        static Context create(
                @Nonnull ClassesMetadataReader<JarFile> classesMetadataReader,
                @Nonnull String mavenRepositoryBaseUrl,
                @Nullable PasswordAuthentication mavenRepositoryCredentials) {
            return new Context(classesMetadataReader, mavenRepositoryBaseUrl, new MavenStaxReader(), new MavenGraphNodeBuilder(), mavenRepositoryCredentials);
        }
    }
}
