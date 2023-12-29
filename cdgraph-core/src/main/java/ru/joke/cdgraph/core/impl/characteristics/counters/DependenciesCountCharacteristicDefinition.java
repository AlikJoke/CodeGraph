package ru.joke.cdgraph.core.impl.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

public final class DependenciesCountCharacteristicDefinition implements CodeGraphCharacteristicDefinition<DependenciesCountCharacteristic, SingleModuleCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "dependencies.count";
    }

    @Nonnull
    @Override
    public DependenciesCountCharacteristic createParameterizedCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new DependenciesCountCharacteristic(parameters);
    }
}
