package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class AllClassesDuplicatesLocationsCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<AllClassesDuplicatesLocationsCharacteristic, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<AllClassesDuplicatesLocationsCharacteristic, CodeGraphCharacteristicParameters> createDefinition() {
        return new AllClassesDuplicatesLocationsCharacteristicDefinition();
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
