package ru.joke.cdgraph.core.std.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.std.characteristics.SimpleCodeGraphCharacteristicResult;

import javax.annotation.Nonnull;

public final class DependenciesCountCharacteristic implements CodeGraphCharacteristic<DependenciesCount> {

    private final DependenciesCountCharacteristicParameters parameters;

    public DependenciesCountCharacteristic(@Nonnull DependenciesCountCharacteristicParameters parameters) {
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<DependenciesCount> compute(@Nonnull CodeGraph graph) {
       final var targetNode = graph.findNodeById(this.parameters.nodeId())
                                    .orElseThrow(() -> new CodeGraphComputationException("Node '" + this.parameters.nodeId() + "' not found"));

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
