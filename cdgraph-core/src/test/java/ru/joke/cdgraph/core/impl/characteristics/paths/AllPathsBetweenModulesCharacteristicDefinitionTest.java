package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class AllPathsBetweenModulesCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<AllPathsBetweenModulesCharacteristic, PathBetweenModulesCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<AllPathsBetweenModulesCharacteristic, PathBetweenModulesCharacteristicParameters> createDefinition() {
        return new AllPathsBetweenModulesCharacteristicDefinition();
    }

    @Override
    protected PathBetweenModulesCharacteristicParameters getParameters() {
        return new PathBetweenModulesCharacteristicParameters("test1", "test2");
    }
}
