package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Set;

public interface CodeGraphCharacteristicFactoriesLoader {

    @Nonnull
    Set<CodeGraphCharacteristicFactory<?, ?, ?>> load();
}
