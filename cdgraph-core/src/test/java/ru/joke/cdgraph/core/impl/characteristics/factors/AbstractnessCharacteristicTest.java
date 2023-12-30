package ru.joke.cdgraph.core.impl.characteristics.factors;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.SingleNodeCharacteristicTestBase;

import javax.annotation.Nonnull;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class AbstractnessCharacteristicTest extends SingleNodeCharacteristicTestBase<Factor> {

    @Test
    public void testAbstractness() throws URISyntaxException {
        final var codeGraph = createCodeGraph();

        final var params = new SingleModuleCharacteristicParameters(TEST_MODULE_3);
        final var characteristic = createCharacteristic(params);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Abstractness factor object must be not null");
        assertEquals(3.0 / 7, result.get().factor(), "Abstractness factor must be equal");
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<Factor> createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new AbstractnessCharacteristic("1", parameters);
    }

    @Nonnull
    @Override
    protected CodeGraph createCodeGraph() throws URISyntaxException {
        return createCodeGraphByJar(TEST_JAR_3_PATH);
    }
}
