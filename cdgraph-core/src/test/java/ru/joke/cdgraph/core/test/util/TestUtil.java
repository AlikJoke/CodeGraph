package ru.joke.cdgraph.core.test.util;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceException;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.datasources.impl.CodeGraphJarDataSource;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph;
import ru.joke.cdgraph.core.graph.impl.maven.MavenModuleCodeGraph;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public abstract class TestUtil {

    public static final String TEST_JAR_2_PATH = "/ds/test-modules2.jar";
    public static final String TEST_JAR_3_PATH = "/ds/test-modules3.jar";
    public static final String TEST_WAR_PATH = "/ds/test-modules.war";
    public static final String TEST_EAR_PATH = "/ds/test-modules.ear";

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
            @Nonnull
            @Override
            public String id() {
                return "test";
            }

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

    public static File getJarFile(final String jarPath) throws URISyntaxException {
        final URL testJarUrl = TestUtil.class.getResource(jarPath);
        final URI testJarUri = Objects.requireNonNull(testJarUrl, "testJarUrl").toURI();

        return new File(testJarUri);
    }

    public static Path unpackTestJarToDirectory(final String testJarPath) throws IOException, URISyntaxException {

        try (final JarFile jarFile = new JarFile(getJarFile(testJarPath), false)) {
            final Path moduleDirPath = Files.createTempDirectory(UUID.randomUUID().toString());
            final File moduleDirFile = moduleDirPath.toFile();
            final File classesDirFile = new File(moduleDirFile, "target/classes");
            classesDirFile.mkdirs();

            moduleDirFile.deleteOnExit();

            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry jarEntry = entries.nextElement();
                final boolean isPomFile = jarEntry.getName().endsWith(MavenModuleCodeGraph.POM_XML);
                final File file = isPomFile
                        ? new File(moduleDirFile, MavenModuleCodeGraph.POM_XML)
                        : new File(classesDirFile, jarEntry.getName());
                if (jarEntry.isDirectory()) {
                    file.mkdirs();
                } else {
                    try (final InputStream is = jarFile.getInputStream(jarEntry);
                            final FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(is.readAllBytes());
                    }
                }
            }

            return moduleDirPath;
        }
    }

    private static CodeGraph createCodeGraphByJar(@Nonnull String jarResourcePath) throws URISyntaxException {
        final var jarUrl = TestUtil.class.getResource(jarResourcePath);
        final var jarPath = Path.of(jarUrl.toURI());
        return new JavaModuleCodeGraph(new CodeGraphJarDataSource(jarPath, new JarClassesMetadataReader()));
    }

    private TestUtil() {
    }
}
