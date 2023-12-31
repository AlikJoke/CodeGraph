package ru.joke.cdgraph.core.impl.util;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceException;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphJarDataSource;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class TestUtil {

    public static final String TEST_JAR_2_PATH = "/ds/test-modules2.jar";
    public static final String TEST_JAR_3_PATH = "/ds/test-modules3.jar";
    public static final String TEST_WAR_PATH = "/ds/test-modules.war";

    public static final String TEST_MODULE_1 = "ru.joke.cdgraph.test_modules1";
    public static final String TEST_MODULE_2 = "ru.joke.cdgraph.test_modules2";
    public static final String TEST_MODULE_3 = "ru.joke.cdgraph.test_modules3";

    public static final String SQL_MODULE = "java.sql";
    public static final String BASE_MODULE = "java.base";

    public static final String TEST_MODULE_1_PATH = "modules/module-info1.class";
    public static final String TEST_MODULE_2_PATH = "modules/module-info2.class";
    public static final String TEST_MODULE_3_PATH = "modules/module-info3.class";

    public static CodeGraphDataSource createCodeGraphDatasource(@Nonnull final String... modulePath) {
        return new CodeGraphDataSource() {
            @Override
            @Nonnull
            public List<Configuration> find(@Nonnull Predicate<String> filter) {
                try {
                    final List<File> result = new ArrayList<>(modulePath.length);
                    for (final String path : modulePath) {
                        final URL moduleDescriptorUrl = getClass().getClassLoader().getResource(path);
                        final File moduleDescriptorFile = new File(moduleDescriptorUrl.getFile());
                        final Path tempCopyFilePath = Files.createTempFile(UUID.randomUUID().toString(), null);
                        Files.copy(moduleDescriptorFile.toPath(), tempCopyFilePath, StandardCopyOption.REPLACE_EXISTING);

                        result.add(tempCopyFilePath.toFile());
                    }

                    return result
                            .stream()
                            .map(config -> new Configuration(config, Collections.emptySet()))
                            .toList();
                } catch (IOException ex) {
                    throw new CodeGraphDataSourceException(ex);
                }
            }
        };
    }

    public static CodeGraph createCodeGraphByJar(@Nonnull String rootJarResourcePath, @Nonnull String... additionalJarResourcePaths) throws URISyntaxException {
        final CodeGraph rootJarGraph = createCodeGraphByJar(rootJarResourcePath);
        final Map<String, GraphNode> allNodesMap =
                rootJarGraph.findAllNodes()
                            .stream()
                            .collect(Collectors.toMap(GraphNode::id, Function.identity()));
        for (final String jarResourcePath : additionalJarResourcePaths) {
            final var codeGraph = createCodeGraphByJar(jarResourcePath);

            codeGraph.findAllNodes().forEach(node -> allNodesMap.put(node.id(), node));
        }

        return new CodeGraph() {
            @Nonnull
            @Override
            public GraphNode findRootNode() {
                return rootJarGraph.findRootNode();
            }

            @Nonnull
            @Override
            public Optional<GraphNode> findNodeById(@Nonnull String id) {
                return Optional.ofNullable(allNodesMap.get(id));
            }

            @Nonnull
            @Override
            public Collection<GraphNode> findAllNodes() {
                return allNodesMap.values();
            }
        };
    }

    private static CodeGraph createCodeGraphByJar(@Nonnull String jarResourcePath) throws URISyntaxException {
        final var jarUrl = TestUtil.class.getResource(jarResourcePath);
        final var jarPath = Path.of(jarUrl.toURI());
        return new JavaModuleCodeGraph(new CodeGraphJarDataSource(jarPath, new JarClassesMetadataReader()));
    }

    private TestUtil() {
    }
}
