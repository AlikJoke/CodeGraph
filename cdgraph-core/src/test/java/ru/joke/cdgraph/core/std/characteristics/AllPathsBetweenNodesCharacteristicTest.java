package ru.joke.cdgraph.core.std.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.std.characteristics.paths.AllPathsBetweenNodesCharacteristic;
import ru.joke.cdgraph.core.std.characteristics.paths.PathBetweenNodes;
import ru.joke.cdgraph.core.std.characteristics.paths.PathBetweenNodesCharacteristicParameters;
import ru.joke.cdgraph.core.std.jms.JavaModuleCodeGraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.std.util.TestUtil.*;

public class AllPathsBetweenNodesCharacteristicTest {

    @Test
    public void testWhenSourceNodeNotFoundThenException() {
        testWhenOneOfNodeNotFound("test", SQL_MODULE);
    }

    @Test
    public void testWhenTargetNodeNotFoundThenException() {
        testWhenOneOfNodeNotFound(SQL_MODULE, "test");
    }

    @Test
    public void testWhenTargetAndSourceAreSameThenException() {
        assertThrows(
                CodeGraphCharacteristicConfigurationException.class,
                () -> new PathBetweenNodesCharacteristicParameters(SQL_MODULE, SQL_MODULE)
        );
    }

    @Test
    public void testWhenNoPathExistBetweenNodes() {
        final var params = new PathBetweenNodesCharacteristicParameters(TEST_MODULE_1, SQL_MODULE);
        final var characteristic = new AllPathsBetweenNodesCharacteristic(params);

        final var codeGraphDatasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH);
        final var result = characteristic.compute(new JavaModuleCodeGraph(codeGraphDatasource));

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertTrue(paths.isEmpty(), "Paths must be empty");
        assertEquals("[]", result.toJson(), "Json must be equal for empty path");
    }

    @Test
    public void testWhenOnlyOnePathExist() {
        final var params = new PathBetweenNodesCharacteristicParameters(TEST_MODULE_3, TEST_MODULE_1);
        final var characteristic = new AllPathsBetweenNodesCharacteristic(params);

        final var codeGraphDatasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(new JavaModuleCodeGraph(codeGraphDatasource));

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(1, paths.size(), "Should exist only 1 path");

        final var relations = paths.get(0).relationsInPath();
        assertEquals(2, relations.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations.get(0).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(0).target().id(), "Target module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(1).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_1, relations.get(1).target().id(), "Target module must be equal");

        final var nodes = paths.get(0).nodesInPath();
        assertEquals(3, nodes.size(), "Nodes path size must be equal");

        assertEquals(TEST_MODULE_3, nodes.get(0).id(), "First module must be equal");
        assertEquals(TEST_MODULE_2, nodes.get(1).id(), "Second module must be equal");
        assertEquals(TEST_MODULE_1, nodes.get(2).id(), "Third module must be equal");
    }

    @Test
    public void testWhenMultiplePathsExists() {
        final var params = new PathBetweenNodesCharacteristicParameters(TEST_MODULE_3, BASE_MODULE);
        final var characteristic = new AllPathsBetweenNodesCharacteristic(params);

        final var codeGraphDatasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(new JavaModuleCodeGraph(codeGraphDatasource));

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(11, paths.size(), "Should exist 5 paths");

        final PathBetweenNodes path1 = paths.get(0);

        final var relations1 = path1.relationsInPath();
        assertEquals(1, relations1.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations1.get(0).source().id(), "Source module must be equal");
        assertEquals(BASE_MODULE, relations1.get(0).target().id(), "Target module must be equal");

        final var nodes1 = path1.nodesInPath();
        assertEquals(2, nodes1.size(), "Nodes path size must be equal");

        final PathBetweenNodes path2 = paths.get(1);

        final var relations2 = path2.relationsInPath();
        assertEquals(2, relations2.size(), "Relations path size must be equal");

        final PathBetweenNodes path3 = paths.get(2);

        final var relations3 = path3.relationsInPath();
        assertEquals(2, relations3.size(), "Relations path size must be equal");

        makeEqualLengthPathsChecks(paths, new int[] { 3, 4, 5, 6, 7 }, 3);
        makeEqualLengthPathsChecks(paths, new int[] { 8, 9, 10 }, 4);
    }

    private void makeEqualLengthPathsChecks(
            final List<PathBetweenNodes> paths,
            final int[] equalLengthPathsIndexes,
            final int expectedRelationsLength) {

        for (int i : equalLengthPathsIndexes) {
            assertEquals(expectedRelationsLength, paths.get(i).relationsInPath().size(), "Relations path size must be equal");
            assertEquals(expectedRelationsLength + 1, paths.get(i).nodesInPath().size(), "Nodes in path size must be equal");
        }
    }

    private void testWhenOneOfNodeNotFound(final String sourceId, final String targetId) {
        final var params = new PathBetweenNodesCharacteristicParameters(sourceId, targetId);
        final var characteristic = new AllPathsBetweenNodesCharacteristic(params);

        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }
}
