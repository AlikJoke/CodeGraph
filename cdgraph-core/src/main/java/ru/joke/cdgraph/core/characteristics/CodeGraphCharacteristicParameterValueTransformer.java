package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Transformer of the parameter values from the {@link String} representation to the actual parameter type.
 *
 * @param <T> actual type of the parameter value
 * @author Alik
 * @implSpec Implementation should be thread-safe
 * @see CodeGraphCharacteristicParameter
 */
@ThreadSafe
public interface CodeGraphCharacteristicParameterValueTransformer<T> {

    @Nonnull
    T fromString(@Nonnull String value);
}
