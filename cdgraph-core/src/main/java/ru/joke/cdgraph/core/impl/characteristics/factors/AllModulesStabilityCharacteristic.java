package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A characteristic that computes stability (metric from the book 'Clean Architecture')
 * of all modules in the graph.<br>
 * See information about the stability of one module in the Java-doc {@link StabilityCharacteristic}.
 *
 * @author Alik
 *
 * @see AllModulesStabilityCharacteristicFactory
 * @see AllModulesStabilityCharacteristicFactoryHandle
 */
final class AllModulesStabilityCharacteristic implements CodeGraphCharacteristic<Map<String, Factor>> {

    private final String id;
    private final CodeGraphCharacteristicFactoryRegistry registry;

    AllModulesStabilityCharacteristic(
            @Nonnull String id,
            @Nonnull CodeGraphCharacteristicFactoryRegistry registry) {
        this.id = id;
        this.registry = registry;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Map<String, Factor>> compute(@Nonnull CodeGraph graph) {
        final Map<String, Factor> factors =
                graph.findAllNodes()
                        .stream()
                        .map(GraphNode::id)
                        .collect(Collectors.toMap(Function.identity(), nodeId -> computeCharacteristic(nodeId, graph)));
        return new SimpleCodeGraphCharacteristicResult<>(this.id, factors);
    }

    private Factor computeCharacteristic(final String nodeId, final CodeGraph graph) {
        final CodeGraphCharacteristicFactory<CodeGraphCharacteristic<Factor>, Factor, SingleModuleCharacteristicParameters> factory =
                this.registry.find(StabilityCharacteristicFactoryHandle.class);
        final var params = new SingleModuleCharacteristicParameters(nodeId);
        final var characteristic = factory.createCharacteristic(params);
        return characteristic.compute(graph).get();
    }
}
