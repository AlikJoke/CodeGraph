package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.impl.characteristics.clusters.ModuleClusteringCharacteristic;
import ru.joke.cdgraph.core.impl.characteristics.clusters.ModuleClusteringCharacteristicParameters;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class ModuleClusteringCharacteristicTest {

    @Test
    public void test() {
        final var params = new ModuleClusteringCharacteristicParameters(5, 7);
        final var characteristic = new ModuleClusteringCharacteristic(params);

        final var ds = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var codeGraph = new JavaModuleCodeGraph(ds);

        final var result = characteristic.compute(codeGraph);
        final var clusters = result.get();

        assertNotNull(clusters, "Clusters list must be not null");
        assertEquals(5, clusters.size(), "Clusters list must be not null");

        // TODO
    }
}
