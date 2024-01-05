package ru.joke.cdgraph.core.characteristics.impl.factors;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicFactoryRegistry;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class AllModulesAbstractnessCharacteristicTest {

    @Test
    public void test() throws URISyntaxException {
        final var codeGraph = createCodeGraphByJar(TEST_JAR_3_PATH, TEST_JAR_2_PATH);

        final var registry = new SimpleCodeGraphCharacteristicFactoryRegistry();
        registry.register(new AbstractnessCharacteristicFactory());

        final var characteristic = new AllModulesAbstractnessCharacteristic("1", registry);
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
