package ru.joke.cdgraph.core.impl.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

public class DependenciesCountCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<DependenciesCountCharacteristic, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<DependenciesCountCharacteristic, SingleModuleCharacteristicParameters> createDefinition() {
        return new DependenciesCountCharacteristicDefinition();
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
