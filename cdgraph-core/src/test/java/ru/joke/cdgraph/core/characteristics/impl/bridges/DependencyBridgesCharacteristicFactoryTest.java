package ru.joke.cdgraph.core.characteristics.impl.bridges;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.characteristics.impl.CodeGraphCharacteristicFactoryTestBase;

import java.util.Set;

public class DependencyBridgesCharacteristicFactoryTest extends
        CodeGraphCharacteristicFactoryTestBase<DependencyBridgesCharacteristicFactoryDescriptor, DependencyBridgesCharacteristic, Set<GraphNodeRelation>, CodeGraphCharacteristicParameters> {

    @Override
    protected CodeGraphCharacteristicFactory<DependencyBridgesCharacteristic, Set<GraphNodeRelation>, CodeGraphCharacteristicParameters> createFactory() {
        return new DependencyBridgesCharacteristicFactory();
    }

    @Override
    protected Class<DependencyBridgesCharacteristicFactoryDescriptor> getFactoryDescriptorClass() {
        return DependencyBridgesCharacteristicFactoryDescriptor.class;
    }

    @Override
    protected CodeGraphCharacteristicParameters getParameters() {
        return CodeGraphCharacteristicParameters.createEmpty();
    }
}
