package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Collection;

public class ModuleClusteringCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ModuleClusteringCharacteristicFactoryHandle, ModuleClusteringCharacteristic, Collection<Cluster>, ModuleClusteringCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ModuleClusteringCharacteristic, Collection<Cluster>, ModuleClusteringCharacteristicParameters> createFactory() {
        return new ModuleClusteringCharacteristicFactory();
    }

    @Override
    protected Class<ModuleClusteringCharacteristicFactoryHandle> getFactoryHandleClass() {
        return ModuleClusteringCharacteristicFactoryHandle.class;
    }

    @Override
    protected ModuleClusteringCharacteristicParameters getParameters() {
        return new ModuleClusteringCharacteristicParameters(1, 1);
    }
}
