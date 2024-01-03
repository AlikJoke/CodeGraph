package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * A factory of the characteristic that computes module abstractness.
 *
 * @author Alik
 *
 * @see AbstractnessCharacteristic
 * @see AbstractnessCharacteristicFactoryHandle
 */
@AbstractnessCharacteristicFactoryHandle
public final class AbstractnessCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AbstractnessCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Nonnull
    @Override
    public AbstractnessCharacteristic createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new AbstractnessCharacteristic(this.characteristicId, parameters);
    }
}
