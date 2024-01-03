package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * Factory of the characteristic of the graph.
 *
 * @param <T> type of the characteristic
 * @param <V> type of the result value of the characteristic
 * @param <K> type of the characteristic parameters
 *
 * @author Alik
 *
 * @see CodeGraphCharacteristic
 * @see CodeGraphCharacteristicParameters
 */
public interface CodeGraphCharacteristicFactory<T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> {

    /**
     * Creates the characteristic by the parameters.
     * @param parameters the parameters of the characteristic, can not be {@code null}.
     * @return the created characteristic, can not be {@code null}.
     */
    @Nonnull
    T createCharacteristic(@Nonnull K parameters);
}
