package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.List;

public class AllPathsBetweenModulesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllPathsBetweenModulesCharacteristicFactoryDescriptor, AllPathsBetweenModulesCharacteristic, List<PathBetweenModules>, PathBetweenModulesCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllPathsBetweenModulesCharacteristic, List<PathBetweenModules>,  PathBetweenModulesCharacteristicParameters> createFactory() {
        return new AllPathsBetweenModulesCharacteristicFactory();
    }

    @Override
    protected Class<AllPathsBetweenModulesCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return AllPathsBetweenModulesCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected PathBetweenModulesCharacteristicParameters getParameters() {
        return new PathBetweenModulesCharacteristicParameters("test1", "test2");
    }
}
