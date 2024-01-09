package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphTag;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.graph.impl.AbstractCodeGraph.SOURCE_MODULE_TAG;

/**
 * A characteristic that computes abstractness (metric from the book 'Clean Architecture')
 * of all modules in the graph.<br>
 * See information about the abstractness of one module in the Java-doc {@link AbstractnessCharacteristic}.
 *
 * @author Alik
 *
 * @see AllModulesAbstractnessCharacteristicFactory
 * @see AllModulesAbstractnessCharacteristicFactoryDescriptor
 */
final class AllModulesAbstractnessCharacteristic implements CodeGraphCharacteristic<Map<String, Factor>> {

    static final String ABSTRACTNESS_TAG = "abstractness";

    private final String id;
    private final CodeGraphCharacteristicService registry;

    AllModulesAbstractnessCharacteristic(
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
                        .filter(this::isSourceModule)
                        .map(GraphNode::id)
                        .collect(Collectors.toMap(Function.identity(), nodeId -> computeCharacteristic(nodeId, graph)));
        return new SimpleCodeGraphCharacteristicResult<>(this.id, factors) {
            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                final var graphCopy = graph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
                graphCopy.findAllNodes()
                            .forEach(this::addAbstractnessTag);

                return graphCopy;
            }

            private void addAbstractnessTag(final GraphNode node) {
                final var abstractnessFactor = factors.get(node.id());
                final var tag = new SimpleGraphTag<>(ABSTRACTNESS_TAG, abstractnessFactor.factor());
                node.tags().put(tag.name(), tag);
            }
        };
    }

    private boolean isSourceModule(final GraphNode node) {
        @SuppressWarnings("unchecked")
        final var isSourceTag = (GraphTag<Boolean>) node.tags().get(SOURCE_MODULE_TAG);
        return isSourceTag != null && isSourceTag.value();
    }

    private Factor computeCharacteristic(final String nodeId, final CodeGraph graph) {
        final var params = new SingleModuleCharacteristicParameters(nodeId);
        final CodeGraphCharacteristicFactory<CodeGraphCharacteristic<Factor>, Factor, SingleModuleCharacteristicParameters> abstractnessCharacteristicFactory =
                this.registry.findFactory(AbstractnessCharacteristicFactoryDescriptor.class);
        final var characteristic = abstractnessCharacteristicFactory.createCharacteristic(params);
        final var result = characteristic.compute(graph);

        return result.get();
    }
}
