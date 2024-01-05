package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphTag;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

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

    private final String id;
    private final CodeGraphCharacteristicFactoryRegistry registry;

    AllModulesAbstractnessCharacteristic(
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
                        .filter(this::isSourceModule)
                        .map(GraphNode::id)
                        .collect(Collectors.toMap(Function.identity(), nodeId -> computeCharacteristic(nodeId, graph)));
        return new SimpleCodeGraphCharacteristicResult<>(this.id, factors);
    }

    private boolean isSourceModule(final GraphNode node) {
        @SuppressWarnings("unchecked")
        final var isSourceTag = (GraphTag<Boolean>) node.tags().get(SOURCE_MODULE_TAG);
        return isSourceTag != null && isSourceTag.value();
    }

    private Factor computeCharacteristic(final String nodeId, final CodeGraph graph) {
        final var params = new SingleModuleCharacteristicParameters(nodeId);
        final CodeGraphCharacteristicFactory<CodeGraphCharacteristic<Factor>, Factor, SingleModuleCharacteristicParameters> abstractnessCharacteristicFactory =
                this.registry.find(AbstractnessCharacteristicFactoryDescriptor.class);
        final var characteristic = abstractnessCharacteristicFactory.createCharacteristic(params);
        final var result = characteristic.compute(graph);

        return result.get();
    }
}