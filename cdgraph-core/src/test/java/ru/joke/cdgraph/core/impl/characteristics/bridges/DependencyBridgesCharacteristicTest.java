package ru.joke.cdgraph.core.impl.characteristics.bridges;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class DependencyBridgesCharacteristicTest {

    @Test
    public void testWhenNoBridges() {
        final var ds = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var codeGraph = new JavaModuleCodeGraph(ds);

        final var characteristic = new DependencyBridgesCharacteristic();
        final var result = characteristic.compute(codeGraph);

        final var bridges = result.get();
        assertNotNull(bridges, "Bridges must be not null");
        assertTrue(bridges.isEmpty(), "Bridges must be empty");
    }

    @Test
    public void testWhenBridgeExist() {
        final var ds = createCodeGraphDatasource(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH);
        final var sourceCodeGraph = new JavaModuleCodeGraph(ds);
        final var resultCodeGraph = createCodeGraphWithoutBaseModule(sourceCodeGraph);

        final var characteristic = new DependencyBridgesCharacteristic();
        final var result = characteristic.compute(resultCodeGraph);

        final var bridges = result.get();
        assertNotNull(bridges, "Bridges must be not null");
        assertEquals(1, bridges.size(), "Bridges count must be equal");

        final var bridge = bridges.iterator().next();
        final var sqlModule = sourceCodeGraph.findNodeById(SQL_MODULE).orElseThrow();
        final var test2Module = sourceCodeGraph.findNodeById(TEST_MODULE_2).orElseThrow();

        assertEquals(sqlModule, bridge.target(), "Target module in bridge must be equal");
        assertEquals(test2Module, bridge.source(), "Source module in bridge must be equal");
    }

    private CodeGraph createCodeGraphWithoutBaseModule(final CodeGraph sourceGraph) {
        final Set<GraphNode> nodes = new HashSet<>(sourceGraph.findAllNodes());
        sourceGraph.findNodeById(BASE_MODULE).ifPresent(baseModule -> {
            nodes.remove(baseModule);
            nodes.forEach(node -> node.relations().removeAll(findRelationsToBaseModule(node, baseModule)));
        });

        final Map<String, GraphNode> nodesMap =
                nodes.stream()
                        .collect(Collectors.toMap(GraphNode::id, Function.identity()));

        return new CodeGraph() {
            @Nonnull
            @Override
            public GraphNode findRootNode() {
                return sourceGraph.findRootNode();
            }

            @Nonnull
            @Override
            public Optional<GraphNode> findNodeById(@Nonnull String id) {
                return Optional.ofNullable(nodesMap.get(id));
            }

            @Nonnull
            @Override
            public Collection<GraphNode> findAllNodes() {
                return nodes;
            }
        };
    }

    private Set<GraphNodeRelation> findRelationsToBaseModule(final GraphNode node, final GraphNode baseModule) {
        return node.relations()
                    .stream()
                    .filter(relation -> relation.target() == baseModule)
                    .collect(Collectors.toSet());
    }
}