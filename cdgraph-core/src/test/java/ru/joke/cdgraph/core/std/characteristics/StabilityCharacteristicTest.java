package ru.joke.cdgraph.core.std.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.std.characteristics.factors.StabilityCharacteristic;
import ru.joke.cdgraph.core.std.jms.JavaModuleCodeGraph;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.std.util.TestUtil.*;

public class StabilityCharacteristicTest {

    @Test
    public void testWhenNodeNotFoundThenException() {
        final var params = new SingleNodeCharacteristicParameters("test");
        final var characteristic = new StabilityCharacteristic(params);

        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }

    @Test
    public void testStability() {
        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));

        makeStabilityChecks(0.25, TEST_MODULE_2, codeGraph);
        makeStabilityChecks(0.5, TEST_MODULE_1, codeGraph);
        makeStabilityChecks(0.0, TEST_MODULE_3, codeGraph);
        makeStabilityChecks(1.0, BASE_MODULE, codeGraph);
        makeStabilityChecks(1 - (double) 4/6, SQL_MODULE, codeGraph);
    }

    private void makeStabilityChecks(final double expectedStability, final String moduleId, final CodeGraph codeGraph) {

        final var params = new SingleNodeCharacteristicParameters(moduleId);
        final var characteristic = new StabilityCharacteristic(params);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Stability factor object must be not null");
        assertEquals(expectedStability, result.get().factor(), "Stability factor must be equal");
    }
}
