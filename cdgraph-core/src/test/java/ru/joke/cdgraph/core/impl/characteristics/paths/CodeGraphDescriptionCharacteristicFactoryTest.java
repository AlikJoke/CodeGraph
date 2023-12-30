package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.List;

public class CodeGraphDescriptionCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<CodeGraphDescriptionCharacteristicFactoryHandle, CodeGraphDescriptionCharacteristic, List<PathBetweenModules>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<CodeGraphDescriptionCharacteristic, List<PathBetweenModules>, CodeGraphCharacteristicParameters> createFactory() {
        return new CodeGraphDescriptionCharacteristicFactory();
    }

    @Override
    protected Class<CodeGraphDescriptionCharacteristicFactoryHandle> getFactoryHandleClass() {
        return CodeGraphDescriptionCharacteristicFactoryHandle.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
