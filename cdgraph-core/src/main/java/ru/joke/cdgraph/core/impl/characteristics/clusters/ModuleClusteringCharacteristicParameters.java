package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnegative;

public record ModuleClusteringCharacteristicParameters(@Nonnegative int optimizingFactor, @Nonnegative int maxRounds) implements CodeGraphCharacteristicParameters {
}
