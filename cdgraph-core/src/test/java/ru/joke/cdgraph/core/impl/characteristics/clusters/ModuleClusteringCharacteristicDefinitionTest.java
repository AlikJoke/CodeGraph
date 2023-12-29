package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class ModuleClusteringCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<ModuleClusteringCharacteristic, ModuleClusteringCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<ModuleClusteringCharacteristic, ModuleClusteringCharacteristicParameters> createDefinition() {
        return new ModuleClusteringCharacteristicDefinition();
    }

    @Override
    protected ModuleClusteringCharacteristicParameters getParameters() {
        return new ModuleClusteringCharacteristicParameters(1, 1);
    }
}
