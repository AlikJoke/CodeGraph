package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.impl.characteristics.paths.ShortestPathBetweenNodesCharacteristic;
import ru.joke.cdgraph.core.impl.characteristics.paths.PathBetweenNodesCharacteristicParameters;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_MODULE_3_PATH;

public class ShortestPathBetweenNodesCharacteristicTest {

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
        final var characteristic = new ShortestPathBetweenNodesCharacteristic(params);

        final var codeGraphDatasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH);
        final var result = characteristic.compute(new JavaModuleCodeGraph(codeGraphDatasource));

        assertNotNull(result.get(), "Result object must be not null");
        assertTrue(result.get().relationsInPath().isEmpty(), "Relations path must be empty");
        assertTrue(result.get().nodesInPath().isEmpty(), "Nodes path must be empty");
        assertEquals("[]", result.toJson(), "Json must be equal for empty path");
    }

    @Test
    public void testWhenOnlyOnePathExist() {
        final var params = new PathBetweenNodesCharacteristicParameters(TEST_MODULE_3, TEST_MODULE_1);
        final var characteristic = new ShortestPathBetweenNodesCharacteristic(params);

        final var codeGraphDatasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(new JavaModuleCodeGraph(codeGraphDatasource));

        assertNotNull(result.get(), "Result object must be not null");

        final var relations = result.get().relationsInPath();
        assertEquals(2, relations.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations.get(0).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(0).target().id(), "Target module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(1).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_1, relations.get(1).target().id(), "Target module must be equal");

        final var nodes = result.get().nodesInPath();
        assertEquals(3, nodes.size(), "Nodes path size must be equal");

        assertEquals(TEST_MODULE_3, nodes.get(0).id(), "First module must be equal");
        assertEquals(TEST_MODULE_2, nodes.get(1).id(), "Second module must be equal");
        assertEquals(TEST_MODULE_1, nodes.get(2).id(), "Third module must be equal");
    }

    @Test
    public void testWhenTwoPathsExistThenFoundShortest() {
        final var params = new PathBetweenNodesCharacteristicParameters(TEST_MODULE_3, SQL_MODULE);
        final var characteristic = new ShortestPathBetweenNodesCharacteristic(params);

        final var codeGraphDatasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(new JavaModuleCodeGraph(codeGraphDatasource));

        assertNotNull(result.get(), "Result object must be not null");

        final var relations = result.get().relationsInPath();
        assertEquals(1, relations.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations.get(0).source().id(), "Source module must be equal");
        assertEquals(SQL_MODULE, relations.get(0).target().id(), "Target module must be equal");

        final var nodes = result.get().nodesInPath();
        assertEquals(2, nodes.size(), "Nodes path size must be equal");

        assertEquals(TEST_MODULE_3, nodes.get(0).id(), "First module must be equal");
        assertEquals(SQL_MODULE, nodes.get(1).id(), "Second module must be equal");
    }

    private void testWhenOneOfNodeNotFound(final String sourceId, final String targetId) {
        final var params = new PathBetweenNodesCharacteristicParameters(sourceId, targetId);
        final var characteristic = new ShortestPathBetweenNodesCharacteristic(params);

        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }
}
