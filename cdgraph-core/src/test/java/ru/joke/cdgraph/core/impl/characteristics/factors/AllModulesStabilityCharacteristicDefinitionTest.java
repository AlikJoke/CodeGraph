package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class AllModulesStabilityCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<AllModulesStabilityCharacteristic, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<AllModulesStabilityCharacteristic, CodeGraphCharacteristicParameters> createDefinition() {
        return new AllModulesStabilityCharacteristicDefinition();
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
