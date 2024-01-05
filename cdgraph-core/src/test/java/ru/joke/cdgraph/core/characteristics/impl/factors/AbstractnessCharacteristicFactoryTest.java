package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

public class AbstractnessCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AbstractnessCharacteristicFactoryDescriptor, AbstractnessCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AbstractnessCharacteristic, Factor, SingleModuleCharacteristicParameters> createFactory() {
        return new AbstractnessCharacteristicFactory();
    }

    @Override
    protected Class<AbstractnessCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return AbstractnessCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
