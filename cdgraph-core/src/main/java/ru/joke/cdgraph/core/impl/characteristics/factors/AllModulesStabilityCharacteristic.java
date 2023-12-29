package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class AllModulesStabilityCharacteristic implements CodeGraphCharacteristic<Map<String, Factor>> {

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Map<String, Factor>> compute(@Nonnull CodeGraph graph) {
        final Map<String, Factor> factors =
                graph.findAllNodes()
                        .stream()
                        .map(GraphNode::id)
                        .collect(Collectors.toMap(Function.identity(), nodeId -> computeCharacteristic(nodeId, graph)));
        return new SimpleCodeGraphCharacteristicResult<>(factors);
    }

    private Factor computeCharacteristic(final String nodeId, final CodeGraph graph) {
        final var params = new SingleModuleCharacteristicParameters(nodeId);
        final var characteristic = new StabilityCharacteristic(params);
        return characteristic.compute(graph).get();
    }
}
