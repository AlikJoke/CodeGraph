package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicComputationException;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;

import javax.annotation.Nonnull;

/**
 * A characteristic that computes module stability (metric from the book 'Clean Architecture').
 * The stability of the module is calculated as the ratio between the number of output dependencies
 * and the total number of output and input dependencies of the module.<br>
 * Type of the characteristic parameters: {@link SingleModuleCharacteristicParameters}.
 *
 * @author Alik
 *
 * @see StabilityCharacteristicFactory
 * @see StabilityCharacteristicFactoryDescriptor
 */
final class StabilityCharacteristic implements CodeGraphCharacteristic<Factor> {

    static final String STABILITY_TAG = "stability";

    private final String id;
    private final SingleModuleCharacteristicParameters parameters;

    StabilityCharacteristic(
            @Nonnull String id,
            @Nonnull SingleModuleCharacteristicParameters parameters) {
        this.id = id;
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Factor> compute(@Nonnull CodeGraph graph) {
        final var targetNode = graph.findNodeById(this.parameters.moduleId())
                                    .orElseThrow(() -> new CodeGraphCharacteristicComputationException("Node '" + this.parameters.moduleId() + "' not found"));

        final int outputDependencies = targetNode.relations().size();
        final int inputDependencies =
                (int) graph.findAllNodes()
                            .stream()
                            .filter(n -> n.relations()
                                    .stream()
                                    .anyMatch(relation -> targetNode.equals(relation.target())))
                            .count();
        final double stability = 1 - (double) outputDependencies / (inputDependencies + outputDependencies);
        return new SimpleCodeGraphCharacteristicResult<>(this.id, this.parameters, new Factor(stability)) {
            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                final var graphCopy = graph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
                graphCopy.findNodeById(targetNode.id())
                            .ifPresent(this::addStabilityTag);

                return graphCopy;
            }

            private void addStabilityTag(final GraphNode node) {
                final var tag = new SimpleGraphTag<>(STABILITY_TAG, stability);
                node.tags().put(tag.name(), tag);
            }
        };
    }
}