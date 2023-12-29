package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Set;

public interface CodeGraphCharacteristicDefinitionsLoader {

    @Nonnull
    Set<CodeGraphCharacteristicDefinition<?, ?>> load();
}
