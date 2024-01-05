package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

public class StabilityCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<StabilityCharacteristicFactoryDescriptor, StabilityCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<StabilityCharacteristic, Factor, SingleModuleCharacteristicParameters> createFactory() {
        return new StabilityCharacteristicFactory();
    }

    @Override
    protected Class<StabilityCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return StabilityCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
