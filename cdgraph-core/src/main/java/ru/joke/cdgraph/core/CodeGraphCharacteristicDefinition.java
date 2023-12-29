package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphCharacteristicDefinition<T extends CodeGraphCharacteristic<?>, K extends CodeGraphCharacteristicParameters> {

    @Nonnull
    String id();

    @Nonnull
    T createParameterizedCharacteristic(@Nonnull K parameters);
}
