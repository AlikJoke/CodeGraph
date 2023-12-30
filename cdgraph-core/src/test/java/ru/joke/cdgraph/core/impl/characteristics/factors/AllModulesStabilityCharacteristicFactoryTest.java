package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Map;

public class AllModulesStabilityCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllModulesStabilityCharacteristicFactoryHandle, AllModulesStabilityCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllModulesStabilityCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters> createFactory() {
        return new AllModulesStabilityCharacteristicFactory();
    }

    @Override
    protected Class<AllModulesStabilityCharacteristicFactoryHandle> getFactoryHandleClass() {
        return AllModulesStabilityCharacteristicFactoryHandle.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
