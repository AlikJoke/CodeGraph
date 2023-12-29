package ru.joke.cdgraph.core.impl.characteristics.bridges;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public final class DependencyBridgesCharacteristicDefinition implements CodeGraphCharacteristicDefinition<DependencyBridgesCharacteristic, CodeGraphCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "dependency.bridges";
    }

    @Nonnull
    @Override
    public DependencyBridgesCharacteristic createParameterizedCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new DependencyBridgesCharacteristic();
    }
}
