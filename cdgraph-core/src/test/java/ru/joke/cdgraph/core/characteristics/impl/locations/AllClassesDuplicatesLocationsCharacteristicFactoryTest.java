package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class AllClassesDuplicatesLocationsCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor, AllClassesDuplicatesLocationsCharacteristic, Set<ClassDuplicatesLocations>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<AllClassesDuplicatesLocationsCharacteristic, Set<ClassDuplicatesLocations>, CodeGraphCharacteristicParameters> createFactory() {
        return new AllClassesDuplicatesLocationsCharacteristicFactory();
    }

    @Override
    protected Class<AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
