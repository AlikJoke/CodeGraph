package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

public class StabilityCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<StabilityCharacteristic, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<StabilityCharacteristic, SingleModuleCharacteristicParameters> createDefinition() {
        return new StabilityCharacteristicDefinition();
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
