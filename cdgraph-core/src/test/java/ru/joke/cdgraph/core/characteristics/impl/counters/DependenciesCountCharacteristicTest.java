package ru.joke.cdgraph.core.characteristics.impl.counters;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.SingleNodeCharacteristicTestBase;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class DependenciesCountCharacteristicTest extends SingleNodeCharacteristicTestBase<DependenciesCount> {

    @Test
    public void testNodeDependencies() {
        final var params = new SingleModuleCharacteristicParameters(TEST_MODULE_2);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph();
        final var result = characteristic.compute(codeGraph);
        assertNotNull(result.get(), "Dependencies count object must be not null");
        assertEquals(1, result.get().input(), "Input dependencies count must be equal");
        assertEquals(3, result.get().output(), "Output dependencies count must be equal");
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<DependenciesCount> createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new DependenciesCountCharacteristic("1", parameters);
    }

    @Nonnull
    @Override
    protected CodeGraph createCodeGraph() {
        return new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
    }
}
