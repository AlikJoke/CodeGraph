package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;

/**
 * A wrapper for the result of a characteristic computation.
 * @param <T> type of the result
 *
 * @author Alik
 */
public interface CodeGraphCharacteristicResult<T> {

    /**
     * Converts the result value to plain string representation.
     * @return result value in the string representation, can not be {@code null}.
     */
    @Nonnull
    String toPlainString();

    /**
     * Converts the result value to JSON string representation.
     * @return result value in the JSON string representation, can not be {@code null}.
     */
    @Nonnull
    String toJson();

    /**
     * Returns the result value of the computation.
     * @return result value, can be {@code null}.
     */
    T get();

    /**
     * Returns the identifier of the characteristic.
     * @return the identifier of the characteristic, can not be {@code null}.
     */
    @Nonnull
    String characteristicId();
}
