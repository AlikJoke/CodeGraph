package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * A factory of the characteristic that computes module stability.
 *
 * @author Alik
 *
 * @see StabilityCharacteristic
 * @see StabilityCharacteristicFactoryDescriptor
 */
@StabilityCharacteristicFactoryDescriptor
public final class StabilityCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<StabilityCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Nonnull
    @Override
    public StabilityCharacteristic createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new StabilityCharacteristic(this.characteristicId, parameters);
    }
}
