package ru.joke.cdgraph.core.characteristics.impl.clusters;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Collection;

public class ModuleClusteringCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ModuleClusteringCharacteristicFactoryDescriptor, ModuleClusteringCharacteristic, Collection<Cluster>, ModuleClusteringCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ModuleClusteringCharacteristic, Collection<Cluster>, ModuleClusteringCharacteristicParameters> createFactory() {
        return new ModuleClusteringCharacteristicFactory();
    }

    @Override
    protected Class<ModuleClusteringCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return ModuleClusteringCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected ModuleClusteringCharacteristicParameters getParameters() {
        return new ModuleClusteringCharacteristicParameters(1, 1);
    }
}
