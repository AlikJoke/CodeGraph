package ru.joke.cdgraph.core.characteristics.impl.factors;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicService;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class AllModulesStabilityCharacteristicTest {

    @Test
    public void test() {
        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));

        final var service = new SimpleCodeGraphCharacteristicService();
        service.registerFactory(new StabilityCharacteristicFactory());

        final var characteristic = new AllModulesStabilityCharacteristic("1", service);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Stability factors collection must be not null");
        assertEquals(codeGraph.findAllNodes().size(), result.get().size(), "Factors size must be equal");

        final var resultMap = result.get();
        makeStabilityChecks(0.25, resultMap.get(TEST_MODULE_2));
        makeStabilityChecks(0.5, resultMap.get(TEST_MODULE_1));
        makeStabilityChecks(0.0, resultMap.get(TEST_MODULE_3));
        makeStabilityChecks(1.0, resultMap.get(BASE_MODULE));
        makeStabilityChecks(1 - (double) 4/6, resultMap.get(SQL_MODULE));
    }

    private void makeStabilityChecks(final double expectedStability, final Factor actualStability) {
        assertEquals(expectedStability, actualStability.factor(), "Stability factor must be equal");
    }
}
