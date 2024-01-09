package ru.joke.cdgraph.core.characteristics.impl.factors;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.SingleNodeCharacteristicTestBase;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.characteristics.impl.factors.StabilityCharacteristic.STABILITY_TAG;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class StabilityCharacteristicTest extends SingleNodeCharacteristicTestBase<Factor> {

    @Test
    public void testStability() {
        final var codeGraph = createCodeGraph();
        makeStabilityChecks(0.25, TEST_MODULE_2, codeGraph);
        makeStabilityChecks(0.5, TEST_MODULE_1, codeGraph);
        makeStabilityChecks(0.0, TEST_MODULE_3, codeGraph);
        makeStabilityChecks(1.0, BASE_MODULE, codeGraph);
        makeStabilityChecks(1 - (double) 4/6, SQL_MODULE, codeGraph);
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<Factor> createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new StabilityCharacteristic("1", parameters);
    }

    @Nonnull
    @Override
    protected CodeGraph createCodeGraph() {
        return new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
    }

    private void makeStabilityChecks(final double expectedStability, final String moduleId, final CodeGraph codeGraph) {

        final var params = new SingleModuleCharacteristicParameters(moduleId);
        final var characteristic = createCharacteristic(params);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Stability factor object must be not null");
        assertEquals(expectedStability, result.get().factor(), "Stability factor must be equal");

        makeSingleModuleTagChecks(
                codeGraph,
                result.visualizedGraph(),
                Set.of(TEST_MODULE_2, TEST_MODULE_3, TEST_MODULE_1, SQL_MODULE, BASE_MODULE),
                moduleId,
                STABILITY_TAG,
                result.get().factor()
        );
    }
}
