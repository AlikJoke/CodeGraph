package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameterValueTransformer;

import javax.annotation.Nonnull;

/**
 * No-Op transformer implementation of the parameter values (default implementation).
 *
 * @author Alik
 * @see CodeGraphCharacteristicParameterValueTransformer
 */
public final class NoOpCharacteristicParameterValueTransformer implements CodeGraphCharacteristicParameterValueTransformer<String> {
    @Nonnull
    @Override
    public String fromString(@Nonnull String value) {
        return value;
    }
}
