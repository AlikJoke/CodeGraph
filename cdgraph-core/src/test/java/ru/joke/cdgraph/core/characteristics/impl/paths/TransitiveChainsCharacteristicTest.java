package ru.joke.cdgraph.core.characteristics.impl.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicComputationException;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.SingleNodeCharacteristicTestBase;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class TransitiveChainsCharacteristicTest extends SingleNodeCharacteristicTestBase<List<PathBetweenModules>> {

    @Test
    public void testWhenSourceNodeNotFoundThenException() {
        final var params = new SingleModuleCharacteristicParameters("test");
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph();
        assertThrows(CodeGraphCharacteristicComputationException.class, () -> characteristic.compute(codeGraph));
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
        return new TransitiveChainsCharacteristic("1", parameters);
    }

    @Nonnull
    @Override
    protected CodeGraph createCodeGraph() {
        final var datasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        return new JavaModuleCodeGraph(datasource);
    }
}
