package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;

import javax.annotation.Nonnull;

public final class ClassLocationsCharacteristicDefinition implements CodeGraphCharacteristicDefinition<ClassLocationsCharacteristic, ResourceLocationsCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "class.locations";
    }

    @Nonnull
    @Override
    public ClassLocationsCharacteristic createParameterizedCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new ClassLocationsCharacteristic(parameters);
    }
}
