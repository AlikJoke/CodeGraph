package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.List;

public class AllPathsBetweenModulesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllPathsBetweenModulesCharacteristicFactoryHandle, AllPathsBetweenModulesCharacteristic, List<PathBetweenModules>, PathBetweenModulesCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllPathsBetweenModulesCharacteristic, List<PathBetweenModules>,  PathBetweenModulesCharacteristicParameters> createFactory() {
        return new AllPathsBetweenModulesCharacteristicFactory();
    }

    @Override
    protected Class<AllPathsBetweenModulesCharacteristicFactoryHandle> getFactoryHandleClass() {
        return AllPathsBetweenModulesCharacteristicFactoryHandle.class;
    }

    @Override
    protected PathBetweenModulesCharacteristicParameters getParameters() {
        return new PathBetweenModulesCharacteristicParameters("test1", "test2");
    }
}
