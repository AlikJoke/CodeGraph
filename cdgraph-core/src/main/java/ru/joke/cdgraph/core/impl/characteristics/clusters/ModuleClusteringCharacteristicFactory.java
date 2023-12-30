package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Collection;

@ModuleClusteringCharacteristicFactoryHandle
public final class ModuleClusteringCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<ModuleClusteringCharacteristic, Collection<Cluster>, ModuleClusteringCharacteristicParameters> {

    @Nonnull
    @Override
    public ModuleClusteringCharacteristic createCharacteristic(@Nonnull ModuleClusteringCharacteristicParameters parameters) {
        return new ModuleClusteringCharacteristic(this.characteristicId, parameters);
    }
}
