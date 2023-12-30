package ru.joke.cdgraph.core.impl.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

public class DependenciesCountCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<DependenciesCountCharacteristicFactoryHandle, DependenciesCountCharacteristic, DependenciesCount, SingleModuleCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<DependenciesCountCharacteristic, DependenciesCount, SingleModuleCharacteristicParameters> createFactory() {
        return new DependenciesCountCharacteristicFactory();
    }

    @Override
    protected Class<DependenciesCountCharacteristicFactoryHandle> getFactoryHandleClass() {
        return DependenciesCountCharacteristicFactoryHandle.class;
    }

    @Override
    protected SingleModuleCharacteristicParameters getParameters() {
        return new SingleModuleCharacteristicParameters("test");
    }
}
