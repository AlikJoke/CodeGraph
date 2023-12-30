package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import java.util.List;

public class TransitiveChainsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<TransitiveChainsCharacteristicFactoryHandle, TransitiveChainsCharacteristic, List<PathBetweenModules>, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<TransitiveChainsCharacteristic, List<PathBetweenModules>,  SingleModuleCharacteristicParameters> createFactory() {
        return new TransitiveChainsCharacteristicFactory();
    }

    @Override
    protected Class<TransitiveChainsCharacteristicFactoryHandle> getFactoryHandleClass() {
        return TransitiveChainsCharacteristicFactoryHandle.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
