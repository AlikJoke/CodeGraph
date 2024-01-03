package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * A factory of the characteristic that computes module stability.
 *
 * @author Alik
 *
 * @see StabilityCharacteristic
 * @see StabilityCharacteristicFactoryHandle
 */
@StabilityCharacteristicFactoryHandle
public final class StabilityCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<StabilityCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Nonnull
    @Override
    public StabilityCharacteristic createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new StabilityCharacteristic(this.characteristicId, parameters);
    }
}
