package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;

import javax.annotation.Nonnull;

public final class PackageLocationsCharacteristicDefinition implements CodeGraphCharacteristicDefinition<PackageLocationsCharacteristic, ResourceLocationsCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "package.locations";
    }

    @Nonnull
    @Override
    public PackageLocationsCharacteristic createParameterizedCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new PackageLocationsCharacteristic(parameters);
    }
}
