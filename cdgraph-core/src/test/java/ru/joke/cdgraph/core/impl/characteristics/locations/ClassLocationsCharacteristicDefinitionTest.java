package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class ClassLocationsCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<ClassLocationsCharacteristic, ResourceLocationsCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<ClassLocationsCharacteristic, ResourceLocationsCharacteristicParameters> createDefinition() {
        return new ClassLocationsCharacteristicDefinition();
    }

    @Override
    protected ResourceLocationsCharacteristicParameters getParameters() {
        return new ResourceLocationsCharacteristicParameters("test");
    }
}
