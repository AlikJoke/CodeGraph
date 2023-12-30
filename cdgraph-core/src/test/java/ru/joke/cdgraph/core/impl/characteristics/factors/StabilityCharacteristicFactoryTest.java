package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

public class StabilityCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<StabilityCharacteristicFactoryHandle, StabilityCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<StabilityCharacteristic, Factor, SingleModuleCharacteristicParameters> createFactory() {
        return new StabilityCharacteristicFactory();
    }

    @Override
    protected Class<StabilityCharacteristicFactoryHandle> getFactoryHandleClass() {
        return StabilityCharacteristicFactoryHandle.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
