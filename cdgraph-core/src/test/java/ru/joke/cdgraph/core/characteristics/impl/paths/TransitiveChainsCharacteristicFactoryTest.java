package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import java.util.List;

public class TransitiveChainsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<TransitiveChainsCharacteristicFactoryDescriptor, TransitiveChainsCharacteristic, List<PathBetweenModules>, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<TransitiveChainsCharacteristic, List<PathBetweenModules>,  SingleModuleCharacteristicParameters> createFactory() {
        return new TransitiveChainsCharacteristicFactory();
    }

    @Override
    protected Class<TransitiveChainsCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return TransitiveChainsCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
