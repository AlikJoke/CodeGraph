package ru.joke.cdgraph.core.impl.characteristics.bridges;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class DependencyBridgesCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<DependencyBridgesCharacteristic, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<DependencyBridgesCharacteristic, CodeGraphCharacteristicParameters> createDefinition() {
        return new DependencyBridgesCharacteristicDefinition();
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
