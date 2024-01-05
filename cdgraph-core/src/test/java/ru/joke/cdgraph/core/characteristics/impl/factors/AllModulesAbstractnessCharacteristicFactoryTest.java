package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Map;

public class AllModulesAbstractnessCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllModulesAbstractnessCharacteristicFactoryDescriptor, AllModulesAbstractnessCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllModulesAbstractnessCharacteristic, Map<String, Factor>,  CodeGraphCharacteristicParameters> createFactory() {
        return new AllModulesAbstractnessCharacteristicFactory();
    }

    @Override
    protected Class<AllModulesAbstractnessCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return AllModulesAbstractnessCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
