package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class ConflictingDependenciesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<ConflictingDependenciesCharacteristicFactoryHandle, ConflictingDependenciesCharacteristic, Set<ConflictingDependencies>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<ConflictingDependenciesCharacteristic, Set<ConflictingDependencies>,  CodeGraphCharacteristicParameters> createFactory() {
        return new ConflictingDependenciesCharacteristicFactory();
    }

    @Override
    protected Class<ConflictingDependenciesCharacteristicFactoryHandle> getFactoryHandleClass() {
        return ConflictingDependenciesCharacteristicFactoryHandle.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
