package ru.joke.cdgraph.core.impl.characteristics.bridges;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

@DependencyBridgesCharacteristicFactoryHandle
public final class DependencyBridgesCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<DependencyBridgesCharacteristic, Set<GraphNodeRelation>, CodeGraphCharacteristicParameters>, CodeGraphCharacteristicFactoryRegistryAware {

    private CodeGraphCharacteristicFactoryRegistry registry;

    @Nonnull
    @Override
    public DependencyBridgesCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new DependencyBridgesCharacteristic(this.characteristicId, this.registry);
    }

    @Override
    public void setRegistry(@Nonnull CodeGraphCharacteristicFactoryRegistry registry) {
        this.registry = registry;
    }
}
