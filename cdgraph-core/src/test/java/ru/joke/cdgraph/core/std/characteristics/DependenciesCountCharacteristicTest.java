package ru.joke.cdgraph.core.std.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.std.characteristics.counters.DependenciesCountCharacteristic;
import ru.joke.cdgraph.core.std.jms.JavaModuleCodeGraph;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.std.util.TestUtil.*;

public class DependenciesCountCharacteristicTest {

    @Test
    public void testWhenNodeNotFoundThenException() {
        final var params = new SingleNodeCharacteristicParameters("test");
        final var characteristic = new DependenciesCountCharacteristic(params);

        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }

    @Test
    public void testNodeDependencies() {
        final var params = new SingleNodeCharacteristicParameters(TEST_MODULE_2);
        final var characteristic = new DependenciesCountCharacteristic(params);

        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH, TEST_MODULE_3_PATH));
        final var result = characteristic.compute(codeGraph);
        assertNotNull(result.get(), "Dependencies count object must be not null");
        assertEquals(1, result.get().input(), "Input dependencies count must be equal");
        assertEquals(3, result.get().output(), "Output dependencies count must be equal");
    }
}
