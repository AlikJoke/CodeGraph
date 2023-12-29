package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class ConflictingDependenciesCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<ConflictingDependenciesCharacteristic, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<ConflictingDependenciesCharacteristic, CodeGraphCharacteristicParameters> createDefinition() {
        return new ConflictingDependenciesCharacteristicDefinition();
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
