package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.impl.AbstractCodeGraph.SOURCE_MODULE_TAG;

public final class AllModulesAbstractnessCharacteristic implements CodeGraphCharacteristic<Map<String, Factor>> {

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Map<String, Factor>> compute(@Nonnull CodeGraph graph) {
        final Map<String, Factor> factors =
                graph.findAllNodes()
                        .stream()
                        .filter(this::isSourceModule)
                        .map(GraphNode::id)
                        .collect(Collectors.toMap(Function.identity(), nodeId -> computeCharacteristic(nodeId, graph)));
        return new SimpleCodeGraphCharacteristicResult<>(factors);
    }

    private boolean isSourceModule(final GraphNode node) {
        @SuppressWarnings("unchecked")
        final var isSourceTag = (GraphTag<Boolean>) node.tags().get(SOURCE_MODULE_TAG);
        return isSourceTag != null && isSourceTag.value();
    }

    private Factor computeCharacteristic(final String nodeId, final CodeGraph graph) {
        final var params = new SingleModuleCharacteristicParameters(nodeId);
        final var characteristic = new AbstractnessCharacteristic(params);
        return characteristic.compute(graph).get();
    }
}
