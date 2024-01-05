package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class ConflictingDependenciesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ConflictingDependenciesCharacteristicFactoryDescriptor, ConflictingDependenciesCharacteristic, Set<ConflictingDependencies>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ConflictingDependenciesCharacteristic, Set<ConflictingDependencies>,  CodeGraphCharacteristicParameters> createFactory() {
        return new ConflictingDependenciesCharacteristicFactory();
    }

    @Override
    protected Class<ConflictingDependenciesCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return ConflictingDependenciesCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
