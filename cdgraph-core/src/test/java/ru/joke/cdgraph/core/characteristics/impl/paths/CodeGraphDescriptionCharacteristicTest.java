package ru.joke.cdgraph.core.characteristics.impl.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.impl.SPIBasedCodeGraphCharacteristicFactoriesLoader;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicService;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class CodeGraphDescriptionCharacteristicTest {

    @Test
    public void test() {
        final var characteristic = createCharacteristic();

        final var codeGraph = createCodeGraph();
        final var result = characteristic.compute(codeGraph);

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(11, paths.size(), "Paths count must be equal");

        makeGraphCloningChecks(codeGraph, result.visualizedGraph(), Collections.emptySet());
    }

    private CodeGraphCharacteristic<List<PathBetweenModules>> createCharacteristic() {
        final var service = new SimpleCodeGraphCharacteristicService();
        service.registerFactories(new SPIBasedCodeGraphCharacteristicFactoriesLoader());

        return new CodeGraphDescriptionCharacteristic("1", service);
    }

    private CodeGraph createCodeGraph() {
        final var datasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        return new JavaModuleCodeGraph(datasource);
    }
}
