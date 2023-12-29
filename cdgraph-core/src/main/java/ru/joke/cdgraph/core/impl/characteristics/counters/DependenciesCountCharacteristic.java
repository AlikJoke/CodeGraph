package ru.joke.cdgraph.core.impl.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

public final class DependenciesCountCharacteristic implements CodeGraphCharacteristic<DependenciesCount> {

    private final SingleModuleCharacteristicParameters parameters;

    public DependenciesCountCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<DependenciesCount> compute(@Nonnull CodeGraph graph) {
       final var targetNode = graph.findNodeById(this.parameters.moduleId())
                                    .orElseThrow(() -> new CodeGraphComputationException("Node '" + this.parameters.moduleId() + "' not found"));

       final int outputDependencies = targetNode.relations().size();
       final int inputDependencies =
               (int) graph.findAllNodes()
                            .stream()
                            .filter(n -> n.relations()
                                            .stream()
                                            .anyMatch(relation -> targetNode.equals(relation.target())))
                            .count();
       return new SimpleCodeGraphCharacteristicResult<>(new DependenciesCount(inputDependencies, outputDependencies));
    }
}
