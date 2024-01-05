package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.List;

public class CodeGraphDescriptionCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<CodeGraphDescriptionCharacteristicFactoryDescriptor, CodeGraphDescriptionCharacteristic, List<PathBetweenModules>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<CodeGraphDescriptionCharacteristic, List<PathBetweenModules>, CodeGraphCharacteristicParameters> createFactory() {
        return new CodeGraphDescriptionCharacteristicFactory();
    }

    @Override
    protected Class<CodeGraphDescriptionCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return CodeGraphDescriptionCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
