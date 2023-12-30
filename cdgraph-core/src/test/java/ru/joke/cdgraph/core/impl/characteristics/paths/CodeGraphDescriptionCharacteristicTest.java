package ru.joke.cdgraph.core.impl.characteristics.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.impl.characteristics.SPIBasedCodeGraphCharacteristicFactoriesLoader;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class CodeGraphDescriptionCharacteristicTest {

    @Test
    public void test() {
        final var characteristic = createCharacteristic();

        final var codeGraph = createCodeGraph();
        final var result = characteristic.compute(codeGraph);

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(11, paths.size(), "Paths count must be equal");
    }

    private CodeGraphCharacteristic<List<PathBetweenModules>> createCharacteristic() {
        final var registry = new SimpleCodeGraphCharacteristicFactoryRegistry();
        registry.register(new SPIBasedCodeGraphCharacteristicFactoriesLoader());

        return new CodeGraphDescriptionCharacteristic("1", registry);
    }

    private CodeGraph createCodeGraph() {
        final var datasource = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        return new JavaModuleCodeGraph(datasource);
    }
}
