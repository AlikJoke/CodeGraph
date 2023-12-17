package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphCharacteristic<T> {

    @Nonnull
    CodeGraphCharacteristicResult<T> compute(@Nonnull CodeGraph graph);
}
