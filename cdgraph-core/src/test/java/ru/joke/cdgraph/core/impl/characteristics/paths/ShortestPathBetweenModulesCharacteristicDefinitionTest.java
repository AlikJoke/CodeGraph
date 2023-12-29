package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class ShortestPathBetweenModulesCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<ShortestPathBetweenModulesCharacteristic, PathBetweenModulesCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<ShortestPathBetweenModulesCharacteristic, PathBetweenModulesCharacteristicParameters> createDefinition() {
        return new ShortestPathBetweenModulesCharacteristicDefinition();
    }

    @Override
    protected PathBetweenModulesCharacteristicParameters getParameters() {
        return new PathBetweenModulesCharacteristicParameters("test1", "test2");
    }
}
