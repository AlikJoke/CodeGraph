package ru.joke.cdgraph.core.impl.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * A factory of the characteristic that computes module dependencies count.
 *
 * @author Alik
 *
 * @see DependenciesCountCharacteristic
 * @see DependenciesCountCharacteristicFactoryHandle
 */
@DependenciesCountCharacteristicFactoryHandle
public final class DependenciesCountCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<DependenciesCountCharacteristic, DependenciesCount, SingleModuleCharacteristicParameters> {

    @Nonnull
    @Override
    public DependenciesCountCharacteristic createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new DependenciesCountCharacteristic(this.characteristicId, parameters);
    }
}
