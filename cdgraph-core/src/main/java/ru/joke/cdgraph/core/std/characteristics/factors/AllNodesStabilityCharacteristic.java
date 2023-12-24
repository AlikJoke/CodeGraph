package ru.joke.cdgraph.core.std.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.std.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.std.characteristics.SingleNodeCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AllNodesStabilityCharacteristic implements CodeGraphCharacteristic<Map<String, Stability>> {

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Map<String, Stability>> compute(@Nonnull CodeGraph graph) {
        final Map<String, Stability> factors =
                graph.findAllNodes()
                        .stream()
                        .map(GraphNode::id)
                        .collect(Collectors.toMap(Function.identity(), nodeId -> computeCharacteristic(nodeId, graph)));
        return new SimpleCodeGraphCharacteristicResult<>(factors);
    }

    private Stability computeCharacteristic(final String nodeId, final CodeGraph graph) {
        final var params = new SingleNodeCharacteristicParameters(nodeId);
        final var characteristic = new StabilityCharacteristic(params);
        return characteristic.compute(graph).get();
    }
}
