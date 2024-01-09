package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;

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
 * @see AllModulesStabilityCharacteristicFactoryDescriptor
 */
final class AllModulesStabilityCharacteristic implements CodeGraphCharacteristic<Map<String, Factor>> {

    private static final String STABILITY_TAG = "stability";

    private final String id;
    private final CodeGraphCharacteristicService registry;

    AllModulesStabilityCharacteristic(
            @Nonnull String id,
            @Nonnull CodeGraphCharacteristicService registry) {
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
        return new SimpleCodeGraphCharacteristicResult<>(this.id, factors) {
            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                final var graphCopy = graph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
                graphCopy.findAllNodes()
                            .forEach(this::addStabilityTag);

                return graphCopy;
            }

            private void addStabilityTag(final GraphNode node) {
                final var stabilityFactor = factors.get(node.id());
                final var tag = new SimpleGraphTag<>(STABILITY_TAG, stabilityFactor.factor());
                node.tags().put(tag.name(), tag);
            }
        };
    }

    private Factor computeCharacteristic(final String nodeId, final CodeGraph graph) {
        final CodeGraphCharacteristicFactory<CodeGraphCharacteristic<Factor>, Factor, SingleModuleCharacteristicParameters> factory =
                this.registry.findFactory(StabilityCharacteristicFactoryDescriptor.class);
        final var params = new SingleModuleCharacteristicParameters(nodeId);
        final var characteristic = factory.createCharacteristic(params);
        return characteristic.compute(graph).get();
    }
}
