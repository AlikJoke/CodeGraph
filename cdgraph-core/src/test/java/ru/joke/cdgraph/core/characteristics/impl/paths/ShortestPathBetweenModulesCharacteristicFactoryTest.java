package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

public class ShortestPathBetweenModulesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ShortestPathBetweenModulesCharacteristicFactoryDescriptor, ShortestPathBetweenModulesCharacteristic, PathBetweenModules, PathBetweenModulesCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ShortestPathBetweenModulesCharacteristic, PathBetweenModules, PathBetweenModulesCharacteristicParameters> createFactory() {
        return new ShortestPathBetweenModulesCharacteristicFactory();
    }

    @Override
    protected Class<ShortestPathBetweenModulesCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return ShortestPathBetweenModulesCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected PathBetweenModulesCharacteristicParameters getParameters() {
        return new PathBetweenModulesCharacteristicParameters("test1", "test2");
    }
}
