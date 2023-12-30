package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class AllClassesDuplicatesLocationsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllClassesDuplicatesLocationsCharacteristicFactoryHandle, AllClassesDuplicatesLocationsCharacteristic, Set<ClassDuplicatesLocations>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllClassesDuplicatesLocationsCharacteristic, Set<ClassDuplicatesLocations>, CodeGraphCharacteristicParameters> createFactory() {
        return new AllClassesDuplicatesLocationsCharacteristicFactory();
    }

    @Override
    protected Class<AllClassesDuplicatesLocationsCharacteristicFactoryHandle> getFactoryHandleClass() {
        return AllClassesDuplicatesLocationsCharacteristicFactoryHandle.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
