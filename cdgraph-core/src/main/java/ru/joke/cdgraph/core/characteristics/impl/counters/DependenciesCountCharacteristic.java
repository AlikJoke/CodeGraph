package ru.joke.cdgraph.core.characteristics.impl.counters;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicComputationException;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * A characteristic that computes module dependencies count. Dependencies count are the
 * number of incoming dependencies (connections from other modules to the current one)
 * and outgoing ones (from the current module to others). This characteristic allows to find
 * modules that contain too many dependencies.<br>
 * Type of the characteristic parameters: {@link SingleModuleCharacteristicParameters}.
 *
 * @author Alik
 *
 * @see DependenciesCountCharacteristicFactory
 * @see DependenciesCountCharacteristicFactoryDescriptor
 */
final class DependenciesCountCharacteristic implements CodeGraphCharacteristic<DependenciesCount> {

    private final String id;
    private final SingleModuleCharacteristicParameters parameters;

    DependenciesCountCharacteristic(
            @Nonnull String id,
            @Nonnull SingleModuleCharacteristicParameters parameters) {
        this.parameters = parameters;
        this.id = id;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<DependenciesCount> compute(@Nonnull CodeGraph graph) {
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
       return new SimpleCodeGraphCharacteristicResult<>(this.id, this.parameters, new DependenciesCount(inputDependencies, outputDependencies));
    }
}