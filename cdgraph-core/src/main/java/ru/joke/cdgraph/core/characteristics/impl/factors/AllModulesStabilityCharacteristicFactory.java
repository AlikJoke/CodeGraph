package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryRegistryAware;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A factory of the characteristic that computes stability of all modules in the graph.
 *
 * @author Alik
 *
 * @see AllModulesStabilityCharacteristic
 * @see AllModulesStabilityCharacteristicFactoryDescriptor
 */
@AllModulesStabilityCharacteristicFactoryDescriptor
public final class AllModulesStabilityCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AllModulesStabilityCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters>, CodeGraphCharacteristicFactoryRegistryAware {

    private CodeGraphCharacteristicService registry;

    @Nonnull
    @Override
    public AllModulesStabilityCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllModulesStabilityCharacteristic(this.characteristicId, this.registry);
    }

    @Override
    public void setRegistry(@Nonnull CodeGraphCharacteristicService registry) {
        this.registry = registry;
    }
}
