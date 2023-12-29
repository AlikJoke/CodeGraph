package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

public class AbstractnessCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<AbstractnessCharacteristic, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<AbstractnessCharacteristic, SingleModuleCharacteristicParameters> createDefinition() {
        return new AbstractnessCharacteristicDefinition();
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
