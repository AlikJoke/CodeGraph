package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes duplicates of the package (set of modules)
 * with specified name.
 *
 * @author Alik
 *
 * @see PackageLocationsCharacteristic
 * @see PackageLocationsCharacteristicFactoryHandle
 */
@PackageLocationsCharacteristicFactoryHandle
public final class PackageLocationsCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<PackageLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Nonnull
    @Override
    public PackageLocationsCharacteristic createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new PackageLocationsCharacteristic(this.characteristicId, parameters);
    }
}
