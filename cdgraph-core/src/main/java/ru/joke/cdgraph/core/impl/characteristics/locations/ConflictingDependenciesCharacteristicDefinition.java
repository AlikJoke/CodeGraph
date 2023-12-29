package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public final class ConflictingDependenciesCharacteristicDefinition implements CodeGraphCharacteristicDefinition<ConflictingDependenciesCharacteristic, CodeGraphCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "conflicting.dependencies";
    }

    @Nonnull
    @Override
    public ConflictingDependenciesCharacteristic createParameterizedCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new ConflictingDependenciesCharacteristic();
    }
}
