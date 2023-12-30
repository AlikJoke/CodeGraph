package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

@AllClassesDuplicatesLocationsCharacteristicFactoryHandle
public final class AllClassesDuplicatesLocationsCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AllClassesDuplicatesLocationsCharacteristic, Set<ClassDuplicatesLocations>, CodeGraphCharacteristicParameters> {

    @Nonnull
    @Override
    public AllClassesDuplicatesLocationsCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllClassesDuplicatesLocationsCharacteristic(this.characteristicId);
    }
}
