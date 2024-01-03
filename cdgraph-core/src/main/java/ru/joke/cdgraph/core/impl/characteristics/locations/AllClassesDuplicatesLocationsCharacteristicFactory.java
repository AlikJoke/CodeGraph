package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes duplicates of classes between
 * different graph modules.
 *
 * @author Alik
 *
 * @see AllClassesDuplicatesLocationsCharacteristic
 * @see AllClassesDuplicatesLocationsCharacteristicFactoryHandle
 */
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
