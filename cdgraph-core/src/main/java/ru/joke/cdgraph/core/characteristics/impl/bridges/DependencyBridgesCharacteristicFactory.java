package ru.joke.cdgraph.core.characteristics.impl.bridges;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryRegistryAware;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes graph bridges.
 *
 * @author Alik
 *
 * @see DependencyBridgesCharacteristic
 * @see DependencyBridgesCharacteristicFactoryDescriptor
 */
@DependencyBridgesCharacteristicFactoryDescriptor
public final class DependencyBridgesCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements
        CodeGraphCharacteristicFactory<DependencyBridgesCharacteristic, Set<GraphNodeRelation>, CodeGraphCharacteristicParameters>,
        CodeGraphCharacteristicFactoryRegistryAware {

    private CodeGraphCharacteristicService registry;

    @Nonnull
    @Override
    public DependencyBridgesCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new DependencyBridgesCharacteristic(this.characteristicId, this.registry);
    }

    @Override
    public void setRegistry(@Nonnull CodeGraphCharacteristicService registry) {
        this.registry = registry;
    }
}
