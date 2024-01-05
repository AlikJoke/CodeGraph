package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Map;

public class AllModulesStabilityCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllModulesStabilityCharacteristicFactoryDescriptor, AllModulesStabilityCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllModulesStabilityCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters> createFactory() {
        return new AllModulesStabilityCharacteristicFactory();
    }

    @Override
    protected Class<AllModulesStabilityCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return AllModulesStabilityCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
