package ru.joke.cdgraph.core.std.jms;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.*;

import java.lang.module.ModuleDescriptor;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.std.util.TestUtil.*;

public class JavaModuleCodeGraphTest {

    @Test
    public void testSingleModule() {
        final CodeGraphDataSource dataSource = createCodeGraphDatasource(TEST_MODULE_1_PATH);

        final CodeGraph graph = new JavaModuleCodeGraph(dataSource);

        assertNotNull(graph.findRootNode(), "Root node must be not null");
        makeTestModule1Checks(graph.findRootNode());
    }

    @Test
    public void testTwoModules() {
        final CodeGraphDataSource dataSource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH);

        final CodeGraph graph = new JavaModuleCodeGraph(dataSource);
        makeTestModule2Checks(graph.findRootNode());
    }

    @Test
    public void testThreeModules() {

        final CodeGraphDataSource dataSource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);

        final CodeGraph graph = new JavaModuleCodeGraph(dataSource);

        assertNotNull(graph.findRootNode(), "Root node must be not null");
        assertEquals(TEST_MODULE_3, graph.findRootNode().id(), "Root node id must be equal");
        assertEquals(3, graph.findRootNode().relations().size(), "Module should be dependent from 3 modules");

        makeNodeTagsChecks(graph.findRootNode());

        final Map<String, GraphNodeRelation> dependenciesByTarget =
                graph.findRootNode().relations()
                        .stream()
                        .collect(Collectors.toMap(relation -> relation.target().id(), Function.identity()));
        final GraphNodeRelation baseModuleRelation = dependenciesByTarget.get(Object.class.getModule().getName());
        final GraphNodeRelation sqlModuleRelation = dependenciesByTarget.get(Connection.class.getModule().getName());
        final GraphNodeRelation test2ModuleRelation = dependenciesByTarget.get(TEST_MODULE_2);

        makeBaseModuleDependencyChecks(baseModuleRelation, graph.findRootNode(), true);
        makeTestModule2Checks(test2ModuleRelation.target());
        makeTestModuleDependencyChecks(test2ModuleRelation, TEST_MODULE_3, Set.of(ModuleDescriptor.Requires.Modifier.TRANSITIVE));
        makeSqlModuleDependencyChecks(sqlModuleRelation, graph.findRootNode());
    }

    @Test
    public void testNoModules() {
        final CodeGraphDataSource dataSource = createCodeGraphDatasource();
        assertThrows(CodeGraphConfigurationException.class, () -> new JavaModuleCodeGraph(dataSource));
    }

    private void makeTestModule2Checks(final GraphNode test2ModuleNode) {

        assertNotNull(test2ModuleNode, "Root node must be not null");
        assertEquals(TEST_MODULE_2, test2ModuleNode.id(), "Root node id must be equal");
        assertEquals(3, test2ModuleNode.relations().size(), "Module should be dependent from 3 modules");

        makeNodeTagsChecks(test2ModuleNode);

        final var dependenciesByTarget =
                test2ModuleNode.relations()
                                .stream()
                                .collect(Collectors.toMap(relation -> relation.target().id(), Function.identity()));
        final GraphNodeRelation baseModuleRelation = dependenciesByTarget.get(Object.class.getModule().getName());
        final GraphNodeRelation sqlModuleRelation = dependenciesByTarget.get(Connection.class.getModule().getName());
        final GraphNodeRelation test1ModuleRelation = dependenciesByTarget.get(TEST_MODULE_1);

        makeBaseModuleDependencyChecks(baseModuleRelation, test2ModuleNode, true);
        makeTestModule1Checks(test1ModuleRelation.target());
        makeTestModuleDependencyChecks(test1ModuleRelation, TEST_MODULE_2, emptySet());
        makeSqlModuleDependencyChecks(sqlModuleRelation, test2ModuleNode);
    }

    private void makeTestModuleDependencyChecks(
            final GraphNodeRelation testModuleRelation,
            final String expectedSourceModule,
            final Set<ModuleDescriptor.Requires.Modifier> expectedDependencyModifiers) {

        assertEquals(expectedSourceModule, testModuleRelation.source().id(), "Source module must be equal");
        assertEquals(JavaModuleCodeGraph.REQUIRES_TYPE, testModuleRelation.type(), "Relation type must be equal");
        final Map<String, Object> relationTags =
                testModuleRelation.tags()
                                    .stream()
                                    .collect(Collectors.toMap(GraphTag::name, GraphTag::value));
        assertEquals(1 + expectedDependencyModifiers.size(), relationTags.size(), "Count of tags of relation must be equal");
        assertNotNull(relationTags.get(JavaModuleCodeGraph.VERSION_TAG), "Version tag for relation must present");
        expectedDependencyModifiers
                .forEach(modifier -> assertTrue((Boolean) relationTags.get(modifier.name().toLowerCase()), "Tag must present for relation"));
    }

    private void makeTestModule1Checks(final GraphNode testModule1Node) {
        assertEquals(TEST_MODULE_1, testModule1Node.id(), "Root node id must be equal");
        assertEquals(1, testModule1Node.relations().size(), "Module should dependent only from base module");
        makeNodeTagsChecks(testModule1Node);

        final Map<String, GraphNodeRelation> dependenciesByTarget =
                testModule1Node.relations()
                                .stream()
                                .collect(Collectors.toMap(relation -> relation.target().id(), Function.identity()));
        final GraphNodeRelation dependencyRelation = dependenciesByTarget.get(Object.class.getModule().getName());
        makeBaseModuleDependencyChecks(dependencyRelation, testModule1Node, true);
    }

    private void makeSqlModuleDependencyChecks(final GraphNodeRelation sqlModuleRelation, final GraphNode rootNode) {

        assertNotNull(sqlModuleRelation, "Sql relation must be not null");
        assertEquals(rootNode, sqlModuleRelation.source(), "Source node must be equal to root");

        assertEquals(JavaModuleCodeGraph.REQUIRES_TYPE, sqlModuleRelation.type(), "Relation type must be equal");
        assertFalse(sqlModuleRelation.target().relations().isEmpty(), "Dependencies for system target module must be empty");

        final Map<String, GraphNodeRelation> dependenciesByTarget =
                sqlModuleRelation.target().relations()
                                            .stream()
                                            .collect(Collectors.toMap(relation -> relation.target().id(), Function.identity()));
        final GraphNodeRelation baseModuleRelation = dependenciesByTarget.get(Object.class.getModule().getName());
        makeBaseModuleDependencyChecks(baseModuleRelation, sqlModuleRelation.target(), false);

        final Map<String, Object> relationTags =
                sqlModuleRelation.tags()
                                    .stream()
                                    .collect(Collectors.toMap(GraphTag::name, GraphTag::value));
        assertEquals(1, relationTags.size(), "Count of tags of relation must be equal");
        assertNotNull(relationTags.get(JavaModuleCodeGraph.VERSION_TAG), "Version tag for relation must present");
    }

    private void makeBaseModuleDependencyChecks(
            final GraphNodeRelation baseModuleRelation,
            final GraphNode rootNode,
            final boolean hasVersionTag) {

        assertNotNull(baseModuleRelation, "Target module must be base module");
        assertEquals(rootNode, baseModuleRelation.source(), "Source node must be equal to root");

        assertEquals(JavaModuleCodeGraph.REQUIRES_TYPE, baseModuleRelation.type(), "Relation type must be equal");
        assertTrue(baseModuleRelation.target().relations().isEmpty(), "Dependencies for target module must be empty");

        final Map<String, Object> relationTags =
                baseModuleRelation.tags()
                                    .stream()
                                    .collect(Collectors.toMap(GraphTag::name, GraphTag::value));
        assertEquals(hasVersionTag ? 2 : 1, relationTags.size(), "Count of tags of relation must be equal");

        final Boolean mandatedTagValue = (Boolean) relationTags.get(ModuleDescriptor.Requires.Modifier.MANDATED.name().toLowerCase());
        assertTrue(mandatedTagValue != null && mandatedTagValue, "Target module relation must be mandated");
        if (hasVersionTag) {
            assertNotNull(relationTags.get(JavaModuleCodeGraph.VERSION_TAG), "Version tag for relation must present");
        }
    }

    private void makeNodeTagsChecks(final GraphNode rootNode) {

        assertEquals(2, rootNode.tags().size(), "Root node tags count must be equal");
        final Map<String, Object> rootNodeTagsMap =
                rootNode.tags()
                        .stream()
                        .collect(Collectors.toMap(GraphTag::name, GraphTag::value));
        assertNotNull(rootNodeTagsMap.get(JavaModuleCodeGraph.VERSION_TAG), "Version tag must present");

        final Boolean openTagValue = (Boolean) rootNodeTagsMap.get(ModuleDescriptor.Modifier.OPEN.name().toLowerCase());
        assertTrue(openTagValue, "Root module must be open");
    }
}
