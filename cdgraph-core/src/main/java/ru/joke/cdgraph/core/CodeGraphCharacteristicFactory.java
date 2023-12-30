package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphCharacteristicFactory<T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> {

    @Nonnull
    T createCharacteristic(@Nonnull K parameters);
}
