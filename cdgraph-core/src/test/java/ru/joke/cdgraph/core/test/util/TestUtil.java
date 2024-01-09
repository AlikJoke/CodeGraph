package ru.joke.cdgraph.core.test.util;

import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceException;
import ru.joke.cdgraph.core.datasources.impl.CodeGraphJarDataSource;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.graph.impl.AbstractCodeGraph;
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

import static org.junit.jupiter.api.Assertions.*;

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

    public static void makeSingleModuleTagChecks(
            CodeGraph codeGraph,
            CodeGraph visualizedGraph,
            Set<String> excludedFromChecksModules,
            String moduleToCheck,
            String tagName,
            Object expectedResultValue) {

        makeGraphCloningChecks(
                codeGraph,
                visualizedGraph,
                excludedFromChecksModules,
                CodeGraph.CloneOptions.CLEAR_TAGS
        );

        final var moduleToCheckNode = visualizedGraph.findNodeById(moduleToCheck).orElseThrow();
        final var tagValue = moduleToCheckNode.tags().get(tagName);
        assertNotNull(tagValue, "Tag must be not null");
        assertEquals(expectedResultValue, tagValue.value(), "Tag value must be equal");
    }

    public static CodeGraph createCodeGraphByJar(@Nonnull String rootJarResourcePath, @Nonnull String... additionalJarResourcePaths) throws URISyntaxException {
        final CodeGraph rootJarGraph = createCodeGraphByJar(rootJarResourcePath);
        final Map<String, GraphNode> allNodesMap =
                rootJarGraph.findAllNodes()
                            .stream()
                            .collect(Collectors.toMap(GraphNode::id, Function.identity()));
        if (additionalJarResourcePaths.length == 0) {
            return createCodeGraphByJar(rootJarResourcePath);
        }

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

            @Override
            @Nonnull
            public CodeGraph clone(@Nonnull CloneOptions... options) {
                throw new UnsupportedOperationException();
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

    public static void makeGraphCloningChecks(
            CodeGraph sourceGraph,
            Set<String> exceptOfNodes,
            CodeGraph.CloneOptions... options) {
        final var clonedGraph = sourceGraph.clone(options);
        makeGraphCloningChecks(sourceGraph, sourceGraph.clone(options), exceptOfNodes, options);
    }

    public static void makeGraphCloningChecks(
            CodeGraph sourceGraph,
            CodeGraph clonedGraph,
            Set<String> exceptOfNodes,
            CodeGraph.CloneOptions... options) {

        final var optionsSet = options.length == 0
                ? EnumSet.noneOf(CodeGraph.CloneOptions.class)
                : EnumSet.of(options[0], options);

        final var clonedRootNode = clonedGraph.findRootNode();
        assertNotNull(clonedRootNode, "Root node must be not null");
        assertEquals(clonedRootNode.id(), sourceGraph.findRootNode().id(), "Root node id must be equal");

        assertEquals(sourceGraph.findAllNodes().size(), clonedGraph.findAllNodes().size(), "Count of nodes in the cloned graph must be equal");
        clonedGraph.findAllNodes()
                .stream()
                .filter(node -> !exceptOfNodes.contains(node.id()))
                .forEach(clonedNode -> makeNodeChecks(clonedNode, optionsSet, sourceGraph));
    }

    public static void makeNodeChecks(
            final GraphNode clonedNode,
            final EnumSet<CodeGraph.CloneOptions> options,
            final CodeGraph sourceGraph) {

        final var sourceNode = sourceGraph.findNodeById(clonedNode.id()).orElseThrow();
        assertEquals(clonedNode.id(), sourceNode.id(), "Node id must be equal");

        makeNodeTagsChecks(clonedNode, sourceNode, options);

        assertEquals(clonedNode.relations().size(), sourceNode.relations().size(), "Relations size must be equal");

        final var relationsByTarget = sourceNode.relations()
                .stream()
                .collect(Collectors.toMap(relation -> relation.target().id(), Function.identity()));
        clonedNode.relations().forEach(clonedRelation -> {
            final var sourceRelation = relationsByTarget.get(clonedRelation.target().id());

            assertNotNull(sourceRelation, "Source relation must be not null");
            assertEquals(sourceRelation.type(), clonedRelation.type(), "Relation type must be not null");
            assertEquals(sourceRelation.source().id(), clonedRelation.source().id(), "Source node id must be equal");
            assertEquals(sourceRelation.target().id(), clonedRelation.target().id(), "Source node id must be equal");

            makeRelationTagsChecks(clonedRelation, sourceRelation, options);
        });
    }

    private static void makeRelationTagsChecks(
            final GraphNodeRelation clonedRelation,
            final GraphNodeRelation sourceRelation,
            final EnumSet<CodeGraph.CloneOptions> options) {
        if (options.contains(CodeGraph.CloneOptions.CLEAR_TAGS)) {
            assertTrue(clonedRelation.tags().isEmpty(), "Tags of relation must be empty");
        } else {
            assertEquals(clonedRelation.tags(), sourceRelation.tags(), "Tags must be equal");
        }
    }

    private static void makeNodeTagsChecks(
            final GraphNode clonedNode,
            final GraphNode sourceNode,
            final EnumSet<CodeGraph.CloneOptions> options) {

        if (options.contains(CodeGraph.CloneOptions.CLEAR_TAGS)) {
            assertTrue(clonedNode.tags().isEmpty(), "Tags must be empty");
        } else if (options.contains(CodeGraph.CloneOptions.CLEAR_CLASSES_METADATA)) {
            final var sourceTags = sourceNode.tags();
            final var sourceTagsCopy = new HashMap<>(sourceTags);
            sourceTagsCopy.remove(AbstractCodeGraph.CLASSES_METADATA_TAG);

            assertEquals(sourceTagsCopy, clonedNode.tags(), "Tags must be equal");
        } else {
            assertEquals(clonedNode.tags(), sourceNode.tags(), "Tags must be equal");
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
