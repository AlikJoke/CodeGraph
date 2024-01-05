package ru.joke.cdgraph.core.characteristics.impl.counters;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * A factory of the characteristic that computes module dependencies count.
 *
 * @author Alik
 *
 * @see DependenciesCountCharacteristic
 * @see DependenciesCountCharacteristicFactoryDescriptor
 */
@DependenciesCountCharacteristicFactoryDescriptor
public final class DependenciesCountCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<DependenciesCountCharacteristic, DependenciesCount, SingleModuleCharacteristicParameters> {

    @Nonnull
    @Override
    public DependenciesCountCharacteristic createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new DependenciesCountCharacteristic(this.characteristicId, parameters);
    }
}
