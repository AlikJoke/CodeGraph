package ru.joke.cdgraph.core.characteristics.impl.clusters;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameter;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParametersCreator;
import ru.joke.cdgraph.core.characteristics.impl.ToIntCharacteristicParameterValueTransformer;

import javax.annotation.Nonnegative;

/**
 * Parameters for characterizing modules graph clustering.
 *
 * @param optimizingFactor optimization coefficient, below which the number of clusters
 *                         in the graph should not decrease, can not be negative
 * @param maxRounds number of rounds of cluster selection using the Louvain algorithm,
 *                  can not be negative
 */
public record ModuleClusteringCharacteristicParameters(
        @CodeGraphCharacteristicParameter(
                id = "optimizing-factor",
                description = "Optimization coefficient, below which the number of clusters in the graph should not decrease, can not be negative",
                required = true,
                valueTransformer = ToIntCharacteristicParameterValueTransformer.class
        )
        @Nonnegative int optimizingFactor,
        @CodeGraphCharacteristicParameter(
                id = "max-rounds",
                description = "Number of rounds of cluster selection using the Louvain algorithm, can not be negative",
                required = true,
                valueTransformer = ToIntCharacteristicParameterValueTransformer.class
        )
        @Nonnegative int maxRounds
) implements CodeGraphCharacteristicParameters {

    @CodeGraphCharacteristicParametersCreator
    public ModuleClusteringCharacteristicParameters {
    }
}
