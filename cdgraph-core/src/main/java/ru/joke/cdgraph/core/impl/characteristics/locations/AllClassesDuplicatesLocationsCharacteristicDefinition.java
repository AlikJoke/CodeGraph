package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public final class AllClassesDuplicatesLocationsCharacteristicDefinition implements CodeGraphCharacteristicDefinition<AllClassesDuplicatesLocationsCharacteristic, CodeGraphCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "all.classes.duplicates.locations";
    }

    @Nonnull
    @Override
    public AllClassesDuplicatesLocationsCharacteristic createParameterizedCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllClassesDuplicatesLocationsCharacteristic();
    }
}
