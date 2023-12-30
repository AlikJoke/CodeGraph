package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

public class ShortestPathBetweenModulesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ShortestPathBetweenModulesCharacteristicFactoryHandle, ShortestPathBetweenModulesCharacteristic, PathBetweenModules, PathBetweenModulesCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ShortestPathBetweenModulesCharacteristic, PathBetweenModules, PathBetweenModulesCharacteristicParameters> createFactory() {
        return new ShortestPathBetweenModulesCharacteristicFactory();
    }

    @Override
    protected Class<ShortestPathBetweenModulesCharacteristicFactoryHandle> getFactoryHandleClass() {
        return ShortestPathBetweenModulesCharacteristicFactoryHandle.class;
    }

    @Override
    protected PathBetweenModulesCharacteristicParameters getParameters() {
        return new PathBetweenModulesCharacteristicParameters("test1", "test2");
    }
}
