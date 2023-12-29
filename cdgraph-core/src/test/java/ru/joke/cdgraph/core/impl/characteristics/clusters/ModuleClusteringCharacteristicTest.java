package ru.joke.cdgraph.core.impl.characteristics.clusters;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class ModuleClusteringCharacteristicTest {

    @Test
    public void testClusteringToTargetClustersCount() {
        final var params = new ModuleClusteringCharacteristicParameters(5, 7);
        final var characteristic = new ModuleClusteringCharacteristic(params);

        final var ds = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var codeGraph = new JavaModuleCodeGraph(ds);

        final var result = characteristic.compute(codeGraph);
        final var clusters = result.get();

        assertNotNull(clusters, "Clusters list must be not null");
        assertEquals(params.optimizingFactor(), clusters.size(), "Clusters size must be equal to optimizing factor");

        final var clustersByIncludedNodesSize =
                clusters
                        .stream()
                        .collect(Collectors.groupingBy(cluster -> cluster.includedNodes().size()));

        final var testModule1 = codeGraph.findNodeById(TEST_MODULE_1).orElseThrow();
        final var testModule2 = codeGraph.findNodeById(TEST_MODULE_2).orElseThrow();
        final var testModule3 = codeGraph.findNodeById(TEST_MODULE_3).orElseThrow();
        final var sqlModule = codeGraph.findNodeById(SQL_MODULE).orElseThrow();

        final var bigCluster = clustersByIncludedNodesSize.get(4);
        assertNotNull(bigCluster, "Cluster with 4 modules must be not null");
        assertFalse(bigCluster.isEmpty(), "Cluster must be not empty");
        assertEquals(bigCluster.get(0).includedNodes(), Set.of(testModule1, testModule2, testModule3, sqlModule), "Included modules must be equal");
        assertEquals(4, clustersByIncludedNodesSize.get(1).size(), "Clusters with 1 module must be equals");
    }

    @Test
    public void testClusteringWithMinimalOptimizingFactor() {
        final var params = new ModuleClusteringCharacteristicParameters(1, 10);
        final var characteristic = new ModuleClusteringCharacteristic(params);

        final var ds = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var codeGraph = new JavaModuleCodeGraph(ds);

        final var result = characteristic.compute(codeGraph);
        final var clusters = result.get();

        assertNotNull(clusters, "Clusters list must be not null");
        assertEquals(2, clusters.size(), "Clusters size must be equal to optimizing factor plus 1");

        final Cluster resultCluster = clusters.iterator().next();

        assertNotNull(resultCluster, "Result cluster must be not null");
        assertEquals(1, resultCluster.relations().size(), "Cluster relations size must equal");
        assertEquals(codeGraph.findAllNodes().size() - 1, resultCluster.includedNodes().size(), "All nodes except of base module must be located in cluster");
    }
}
