package ru.joke.cdgraph.core.impl.characteristics.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.SingleNodeCharacteristicTestBase;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class TransitiveChainsCharacteristicTest extends SingleNodeCharacteristicTestBase<List<PathBetweenModules>> {

    @Test
    public void testWhenSourceNodeNotFoundThenException() {
        final var params = new SingleModuleCharacteristicParameters("test");
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph();
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }

    @Test
    public void testMultiplePaths() {
        makePathsChecks(TEST_MODULE_2, 6);
        makePathsChecks(TEST_MODULE_3, 11);
    }

    private void makePathsChecks(final String moduleId, final  int expectedChainsCount) {

        final var params = new SingleModuleCharacteristicParameters(moduleId);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph();
        final var result = characteristic.compute(codeGraph);

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(expectedChainsCount, paths.size(), "Chains count must be equal");
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<List<PathBetweenModules>> createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new TransitiveChainsCharacteristic(parameters);
    }

    @Nonnull
    @Override
    protected CodeGraph createCodeGraph() {
        final var datasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        return new JavaModuleCodeGraph(datasource);
    }
}
