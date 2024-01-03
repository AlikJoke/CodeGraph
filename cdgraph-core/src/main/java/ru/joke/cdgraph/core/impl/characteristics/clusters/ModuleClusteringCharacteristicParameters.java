package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnegative;

/**
 * Parameters for characterizing modules graph clustering.
 *
 * @param optimizingFactor optimization coefficient, below which the number of clusters
 *                         in the graph should not decrease, can not be negative
 * @param maxRounds number of rounds of cluster selection using the Louvain algorithm,
 *                  can not be negative
 */
public record ModuleClusteringCharacteristicParameters(@Nonnegative int optimizingFactor, @Nonnegative int maxRounds) implements CodeGraphCharacteristicParameters {
}
