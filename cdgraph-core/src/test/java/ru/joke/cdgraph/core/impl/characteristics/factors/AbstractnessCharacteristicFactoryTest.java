package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

public class AbstractnessCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AbstractnessCharacteristicFactoryHandle, AbstractnessCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AbstractnessCharacteristic, Factor, SingleModuleCharacteristicParameters> createFactory() {
        return new AbstractnessCharacteristicFactory();
    }

    @Override
    protected Class<AbstractnessCharacteristicFactoryHandle> getFactoryHandleClass() {
        return AbstractnessCharacteristicFactoryHandle.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
