package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class PackageLocationsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<PackageLocationsCharacteristicFactoryDescriptor, PackageLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<PackageLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> createFactory() {
        return new PackageLocationsCharacteristicFactory();
    }

    @Override
    protected Class<PackageLocationsCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return PackageLocationsCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected ResourceLocationsCharacteristicParameters getParameters() {
        return new ResourceLocationsCharacteristicParameters("test");
    }
}
