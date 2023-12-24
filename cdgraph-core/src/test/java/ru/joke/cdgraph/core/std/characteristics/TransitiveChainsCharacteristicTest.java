package ru.joke.cdgraph.core.std.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.std.characteristics.paths.TransitiveChainsCharacteristic;
import ru.joke.cdgraph.core.std.jms.JavaModuleCodeGraph;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.std.util.TestUtil.*;

public class TransitiveChainsCharacteristicTest {

    @Test
    public void testWhenSourceNodeNotFoundThenException() {
        final var params = new SingleNodeCharacteristicParameters("test");
        final var characteristic = new TransitiveChainsCharacteristic(params);

        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }

    @Test
    public void testMultiplePaths() {
        makePathsChecks(TEST_MODULE_2, 6);
        makePathsChecks(TEST_MODULE_3, 11);
    }

    private void makePathsChecks(final String moduleId, final  int expectedChainsCount) {

        final var params = new SingleNodeCharacteristicParameters(moduleId);
        final var characteristic = new TransitiveChainsCharacteristic(params);

        final var codeGraphDatasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(new JavaModuleCodeGraph(codeGraphDatasource));

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(expectedChainsCount, paths.size(), "Chains count must be equal");
    }
}
