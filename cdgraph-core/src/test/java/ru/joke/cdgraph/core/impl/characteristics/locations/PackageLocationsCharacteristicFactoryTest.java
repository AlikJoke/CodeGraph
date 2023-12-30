package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class PackageLocationsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<PackageLocationsCharacteristicFactoryHandle, PackageLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<PackageLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> createFactory() {
        return new PackageLocationsCharacteristicFactory();
    }

    @Override
    protected Class<PackageLocationsCharacteristicFactoryHandle> getFactoryHandleClass() {
        return PackageLocationsCharacteristicFactoryHandle.class;
    }

    @Override
    protected ResourceLocationsCharacteristicParameters getParameters() {
        return new ResourceLocationsCharacteristicParameters("test");
    }
}
