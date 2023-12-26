package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.impl.characteristics.factors.AllNodesAbstractnessCharacteristic;
import ru.joke.cdgraph.core.impl.characteristics.factors.Factor;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class AllNodesAbstractnessCharacteristicTest {

    @Test
    public void test() throws URISyntaxException {
        final var codeGraph = createCodeGraphByJar(TEST_JAR_3_PATH, TEST_JAR_2_PATH);
        final var characteristic = new AllNodesAbstractnessCharacteristic();
        final var result = characteristic.compute(codeGraph);

        final var resultMap = result.get();
        assertNotNull(resultMap, "Abstractness map must be not null");
        assertEquals(2, resultMap.size(), "Abstractness map size must be equal");

        makeAbstractnessFactorChecks(resultMap.get(TEST_MODULE_3), 3.0 / 7);
        makeAbstractnessFactorChecks(resultMap.get(TEST_MODULE_2), 0.0);
    }

    private void makeAbstractnessFactorChecks(final Factor actualAbstractnessFactor, final double expectedAbstractness) {
        assertNotNull(actualAbstractnessFactor, "Abstractness factor for module must be not null");
        assertEquals(expectedAbstractness, actualAbstractnessFactor.factor(), "Abstractness factor must be equal");
    }
}
