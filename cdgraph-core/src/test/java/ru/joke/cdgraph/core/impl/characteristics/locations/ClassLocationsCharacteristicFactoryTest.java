package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class ClassLocationsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ClassLocationsCharacteristicFactoryHandle, ClassLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ClassLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> createFactory() {
        return new ClassLocationsCharacteristicFactory();
    }

    @Override
    protected Class<ClassLocationsCharacteristicFactoryHandle> getFactoryHandleClass() {
        return ClassLocationsCharacteristicFactoryHandle.class;
    }

    @Override
    protected ResourceLocationsCharacteristicParameters getParameters() {
        return new ResourceLocationsCharacteristicParameters("test");
    }
}
