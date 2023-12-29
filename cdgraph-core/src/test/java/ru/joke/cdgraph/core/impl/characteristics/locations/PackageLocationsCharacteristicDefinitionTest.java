package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicDefinitionTestBase;

public class PackageLocationsCharacteristicDefinitionTest extends CodeGraphCharacteristicDefinitionTestBase<PackageLocationsCharacteristic, ResourceLocationsCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicDefinition<PackageLocationsCharacteristic, ResourceLocationsCharacteristicParameters> createDefinition() {
        return new PackageLocationsCharacteristicDefinition();
    }

    @Override
    protected ResourceLocationsCharacteristicParameters getParameters() {
        return new ResourceLocationsCharacteristicParameters("test");
    }
}
