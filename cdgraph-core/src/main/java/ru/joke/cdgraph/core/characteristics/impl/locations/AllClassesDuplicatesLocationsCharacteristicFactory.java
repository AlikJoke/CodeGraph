package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes duplicates of classes between
 * different graph modules.
 *
 * @author Alik
 *
 * @see AllClassesDuplicatesLocationsCharacteristic
 * @see AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor
 */
@AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor
public final class AllClassesDuplicatesLocationsCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AllClassesDuplicatesLocationsCharacteristic, Set<ClassDuplicatesLocations>, CodeGraphCharacteristicParameters> {

    @Nonnull
    @Override
    public AllClassesDuplicatesLocationsCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllClassesDuplicatesLocationsCharacteristic(this.characteristicId);
    }
}
