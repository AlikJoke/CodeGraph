package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes duplicates of the package (set of modules)
 * with specified name.
 *
 * @author Alik
 *
 * @see PackageLocationsCharacteristic
 * @see PackageLocationsCharacteristicFactoryDescriptor
 */
@PackageLocationsCharacteristicFactoryDescriptor
public final class PackageLocationsCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<PackageLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Nonnull
    @Override
    public PackageLocationsCharacteristic createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new PackageLocationsCharacteristic(this.characteristicId, parameters);
    }
}
