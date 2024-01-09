package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryRegistryAware;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A factory of the characteristic that computes abstractness of all modules in the graph.
 *
 * @author Alik
 *
 * @see AllModulesAbstractnessCharacteristic
 * @see AllModulesAbstractnessCharacteristicFactoryDescriptor
 */
@AllModulesAbstractnessCharacteristicFactoryDescriptor
public final class AllModulesAbstractnessCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AllModulesAbstractnessCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters>, CodeGraphCharacteristicFactoryRegistryAware {

    private CodeGraphCharacteristicService registry;

    @Nonnull
    @Override
    public AllModulesAbstractnessCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllModulesAbstractnessCharacteristic(this.characteristicId, this.registry);
    }

    @Override
    public void setRegistry(@Nonnull CodeGraphCharacteristicService registry) {
        this.registry = registry;
    }
}
