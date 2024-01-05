package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameterValueTransformer;

import javax.annotation.Nonnull;

/**
 * Transformer implementation of the parameter values to {@link Integer}.
 *
 * @author Alik
 * @see CodeGraphCharacteristicParameterValueTransformer
 */
public final class ToIntCharacteristicParameterValueTransformer implements CodeGraphCharacteristicParameterValueTransformer<Integer> {
    @Nonnull
    @Override
    public Integer fromString(@Nonnull String value) {
        return Integer.parseInt(value);
    }
}
