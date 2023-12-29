package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class AllModulesAbstractnessCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<AllModulesAbstractnessCharacteristic, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<AllModulesAbstractnessCharacteristic, CodeGraphCharacteristicParameters> createDefinition() {
        return new AllModulesAbstractnessCharacteristicDefinition();
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
