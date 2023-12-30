package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Map;

public class AllModulesAbstractnessCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllModulesAbstractnessCharacteristicFactoryHandle, AllModulesAbstractnessCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllModulesAbstractnessCharacteristic, Map<String, Factor>,  CodeGraphCharacteristicParameters> createFactory() {
        return new AllModulesAbstractnessCharacteristicFactory();
    }

    @Override
    protected Class<AllModulesAbstractnessCharacteristicFactoryHandle> getFactoryHandleClass() {
        return AllModulesAbstractnessCharacteristicFactoryHandle.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
