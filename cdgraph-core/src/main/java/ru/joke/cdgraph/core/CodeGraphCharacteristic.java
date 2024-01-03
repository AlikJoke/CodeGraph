package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * A graph characteristic that computes specific metric of a modules graph.
 * @param <T> type of the result value of the characteristic
 *
 * @author Alik
 * @see CodeGraphCharacteristicResult
 * @see CodeGraph
 */
public interface CodeGraphCharacteristic<T> {

    /**
     * Computes the characteristic in the context of the specified modules graph.
     *
     * @param graph modules graph, can not be {@code null}.
     * @return the characteristic result, can not be {@code null}.
     * @see CodeGraphCharacteristicResult
     */
    @Nonnull
    CodeGraphCharacteristicResult<T> compute(@Nonnull CodeGraph graph);
}
