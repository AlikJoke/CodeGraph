package ru.joke.cdgraph.core.characteristics.impl.counters;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

public class DependenciesCountCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<DependenciesCountCharacteristicFactoryDescriptor, DependenciesCountCharacteristic, DependenciesCount, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<DependenciesCountCharacteristic, DependenciesCount, SingleModuleCharacteristicParameters> createFactory() {
        return new DependenciesCountCharacteristicFactory();
    }

    @Override
    protected Class<DependenciesCountCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return DependenciesCountCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
