package ru.joke.cdgraph.core.impl.maven;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphEarDataSourceFactory;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphFileSystemDataSourceFactory;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphJarDataSourceFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.maven.MavenModuleCodeGraph.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.getJarFile;
import static ru.joke.cdgraph.core.impl.util.TestUtil.unpackTestJarToDirectory;

public class MavenModuleCodeGraphTest {

    private static final String TEST_EAR_EXT_PATH = "/ds/test-modules-ext.ear";
    private static final String TEST_SKINNY_JAR_PATH = "/ds/test-skinny-jar.jar";
    private static final String TEST_EAR_ID = "ru.joke.cdgraph.test-modules:test-modules4:1.0-SNAPSHOT";
    private static final String TEST_WAR_ID = "ru.joke.cdgraph.test-modules:test-modules3:1.0-SNAPSHOT";
    private static final String TEST2_MODULE_ID = "ru.joke.cdgraph.test-modules:test-modules2:1.0-SNAPSHOT";
    private static final String TEST1_MODULE_ID = "ru.joke.cdgraph.test-modules:test-modules1:1.0-SNAPSHOT";
    private static final String TEST_JSR305_ID = "com.google.code.findbugs:jsr305:3.0.2";
    private static final String KRYO_ID = "com.esotericsoftware:kryo:5.5.0";
    private static final String OBJENESIS_ID = "org.objenesis:objenesis:3.3";
    private static final String MINLOG_ID = "com.esotericsoftware:minlog:1.3.1";

    @Test
    public void testEarGraph() throws URISyntaxException {
        final var path = createPath(TEST_EAR_EXT_PATH);
        final var ds = new CodeGraphEarDataSourceFactory().create(path);

        final var codeGraph = new MavenModuleCodeGraphFactory().create(ds);

        assertNotNull(codeGraph, "CodeGraph must be not null");

        final var rootNode = codeGraph.findRootNode();
        assertNotNull(rootNode, "CodeGraph root node must be not null");

        makeEarModuleChecks(codeGraph, rootNode);
    }

    @Test
    public void testSkinnyJar() throws URISyntaxException {

        final var path = createPath(TEST_SKINNY_JAR_PATH);
        final var ds = new CodeGraphJarDataSourceFactory().create(path);

        final var codeGraph = new MavenModuleCodeGraphFactory().create(ds);

        assertNotNull(codeGraph, "CodeGraph must be not null");
        makeTest1ModuleChecks(codeGraph, codeGraph.findRootNode());
    }

    @Test
    public void testCompiledSources() throws URISyntaxException, IOException {

        final var path = unpackTestJarToDirectory(TEST_SKINNY_JAR_PATH);
        final var ds = new CodeGraphFileSystemDataSourceFactory().create(path);

        final var codeGraph = new MavenModuleCodeGraphFactory().create(ds);

        assertNotNull(codeGraph, "CodeGraph must be not null");
        makeTest1ModuleChecks(codeGraph, codeGraph.findRootNode());
    }

    private Path createPath(final String path) throws URISyntaxException {
        return getJarFile(path).toPath();
    }

    private void makeEarModuleChecks(final CodeGraph codeGraph, final GraphNode earNode) {
        assertEquals(TEST_EAR_ID, earNode.id(), "Root node must be ear");
        assertEquals(1, earNode.relations().size(), "Relations size of ear module must be equal");
        assertEquals(8, codeGraph.findAllNodes().size(), "CodeGraph nodes size must be equal");

        makeModuleNodeTagsChecks(earNode, null, null, EAR_TYPE);

        final var relationToWar = earNode.relations().iterator().next();
        final var warNode = relationToWar.target();

        makeWarRelationTagsChecks(relationToWar);
        assertEquals(earNode, relationToWar.source(), "Source of relation must be equal");
        makeWarModuleChecks(codeGraph, warNode);
    }

    private void makeWarModuleChecks(final CodeGraph codeGraph, final GraphNode warNode) {

        assertEquals(TEST_WAR_ID, warNode.id(), "Target of relation must be equal");
        assertEquals(3, warNode.relations().size(), "Relations count must be equal");

        makeModuleNodeTagsChecks(warNode, null, null, WAR_TYPE);

        final Map<String, GraphNode> relationsByTarget =
                warNode.relations()
                        .stream()
                        .collect(Collectors.toMap(relation -> relation.target().id(), GraphNodeRelation::target));
        final var jsr305Node = relationsByTarget.get(TEST_JSR305_ID);
        assertNotNull(jsr305Node, "Relation to node must exist");
        assertEquals(0, jsr305Node.relations().size(), "Relations count must be equal");

        assertNotNull(relationsByTarget.get(TEST1_MODULE_ID), "Relation to module must exist");

        final var test2ModuleNode = relationsByTarget.get(TEST2_MODULE_ID);

        assertNotNull(test2ModuleNode, "Relation to module must exist");
        makeTest2ModuleChecks(codeGraph, test2ModuleNode);
    }

    private void makeTest2ModuleChecks(final CodeGraph codeGraph, final GraphNode test2ModuleNode) {

        assertEquals(1, test2ModuleNode.relations().size(), "Relations count must be equal");
        makeModuleNodeTagsChecks(test2ModuleNode, null, null, null);

        final var test1ModuleNode = codeGraph.findNodeById(TEST1_MODULE_ID).orElseThrow();
        final var relationToTest1Node = test2ModuleNode.relations().iterator().next();

        assertEquals(test1ModuleNode, relationToTest1Node.target(), "Target node must be equal");
        makeTest1ModuleChecks(codeGraph, test1ModuleNode);
    }

    private void makeTest1ModuleChecks(final CodeGraph codeGraph, final GraphNode test1ModuleNode) {

        assertEquals(1, test1ModuleNode.relations().size(), "Relations count must be equal");
        makeModuleNodeTagsChecks(test1ModuleNode, "Test1", "Test1", null);

        final var kryoNode = codeGraph.findNodeById(KRYO_ID).orElseThrow();
        final var test1ModuleNodeRelations = test1ModuleNode.relations().iterator().next();
        assertEquals(kryoNode, test1ModuleNodeRelations.target(), "Target node must be equal");
        assertEquals(2, kryoNode.relations().size(), "Relations count must be equal");

        final Map<String, GraphNode> relationsByTargetFromKryo =
                kryoNode.relations()
                        .stream()
                        .collect(Collectors.toMap(relation -> relation.target().id(), GraphNodeRelation::target));

        final var minLogNode = codeGraph.findNodeById(MINLOG_ID).orElseThrow();
        assertEquals(minLogNode, relationsByTargetFromKryo.get(MINLOG_ID), "Target node must be equal");
        assertTrue(minLogNode.relations().isEmpty(), "Relations must be empty");

        final var objenesisNode = codeGraph.findNodeById(OBJENESIS_ID).orElseThrow();
        assertEquals(objenesisNode, relationsByTargetFromKryo.get(OBJENESIS_ID), "Target node must be equal");
        assertTrue(objenesisNode.relations().isEmpty(), "Relations must be empty");
    }

    private void makeModuleNodeTagsChecks(
            final GraphNode moduleNode,
            final String moduleName,
            final String description,
            final String packaging) {
        final var tags = moduleNode.tags();
        final var moduleNameTag = tags.get(MODULE_NAME_TAG);

        if (moduleName != null) {
            assertNotNull(moduleNameTag, "Module name tag must be not null");
            assertEquals(moduleName, moduleNameTag.value(), "Module name tag must be equal");
        }

        final var descriptionTag = tags.get(MODULE_DESCRIPTION_TAG);
        if (description != null) {
            assertNotNull(descriptionTag, "Description tag must be not null");
            assertEquals(description, descriptionTag.value(), "Description tag must be equal");
        }

        final var packagingTag = tags.get(MODULE_PACKAGING_TAG);
        if (packaging != null) {
            assertNotNull(packagingTag, "Packaging tag must be not null");
            assertEquals(packaging, packagingTag.value(), "Packaging tag must be equal");
        }

        final var groupTag = tags.get(MODULE_GROUP_TAG);
        final String[] moduleIdParts = moduleNode.id().split(":");
        assertEquals(moduleIdParts[0], groupTag.value(), "Group must be equal");
    }

    private void makeWarRelationTagsChecks(final GraphNodeRelation relation) {
        final var tags = relation.tags();
        final var dependencyTypeTag = tags.get(DEPENDENCY_TYPE_TAG);

        assertNotNull(dependencyTypeTag, "Dependency type tag must be not null");
        assertEquals(WAR_TYPE, dependencyTypeTag.value(), "Dependency type must be equal");
    }
}
