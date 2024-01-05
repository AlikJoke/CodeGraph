package ru.joke.cdgraph.core.characteristics.impl.clusters;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * A factory of the characteristic that computes graph clusters.
 *
 * @author Alik
 *
 * @see ModuleClusteringCharacteristic
 * @see ModuleClusteringCharacteristicFactoryDescriptor
 */
@ModuleClusteringCharacteristicFactoryDescriptor
public final class ModuleClusteringCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<ModuleClusteringCharacteristic, Collection<Cluster>, ModuleClusteringCharacteristicParameters> {

    @Nonnull
    @Override
    public ModuleClusteringCharacteristic createCharacteristic(@Nonnull ModuleClusteringCharacteristicParameters parameters) {
        return new ModuleClusteringCharacteristic(this.characteristicId, parameters);
    }
}
