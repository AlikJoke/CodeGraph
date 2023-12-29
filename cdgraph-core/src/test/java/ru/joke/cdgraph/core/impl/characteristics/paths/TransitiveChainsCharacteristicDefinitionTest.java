package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

public class TransitiveChainsCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<TransitiveChainsCharacteristic, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<TransitiveChainsCharacteristic, SingleModuleCharacteristicParameters> createDefinition() {
        return new TransitiveChainsCharacteristicDefinition();
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
