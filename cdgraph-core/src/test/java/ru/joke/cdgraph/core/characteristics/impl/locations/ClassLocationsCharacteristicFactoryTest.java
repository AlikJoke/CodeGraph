package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class ClassLocationsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ClassLocationsCharacteristicFactoryDescriptor, ClassLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ClassLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> createFactory() {
        return new ClassLocationsCharacteristicFactory();
    }

    @Override
    protected Class<ClassLocationsCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return ClassLocationsCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected ResourceLocationsCharacteristicParameters getParameters() {
        return new ResourceLocationsCharacteristicParameters("test");
    }
}
