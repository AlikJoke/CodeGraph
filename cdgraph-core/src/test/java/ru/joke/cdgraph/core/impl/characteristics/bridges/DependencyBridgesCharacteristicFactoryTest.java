package ru.joke.cdgraph.core.impl.characteristics.bridges;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.impl.characteristics.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class DependencyBridgesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<DependencyBridgesCharacteristicFactoryHandle, DependencyBridgesCharacteristic, Set<GraphNodeRelation>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<DependencyBridgesCharacteristic, Set<GraphNodeRelation>, CodeGraphCharacteristicParameters> createFactory() {
        return new DependencyBridgesCharacteristicFactory();
    }

    @Override
    protected Class<DependencyBridgesCharacteristicFactoryHandle> getFactoryHandleClass() {
        return DependencyBridgesCharacteristicFactoryHandle.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
