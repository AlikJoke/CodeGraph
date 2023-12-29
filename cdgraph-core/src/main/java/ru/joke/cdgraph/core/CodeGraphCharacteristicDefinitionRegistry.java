package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphCharacteristicDefinitionRegistry {

    <T extends CodeGraphCharacteristic<?>, K extends CodeGraphCharacteristicParameters> void register(@Nonnull CodeGraphCharacteristicDefinition<T, K> characteristicDefinition);

    void register(@Nonnull CodeGraphCharacteristicDefinitionsLoader loader);

    @Nonnull
    <T extends CodeGraphCharacteristic<?>, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicDefinition<T, K> find(@Nonnull String id);
}
