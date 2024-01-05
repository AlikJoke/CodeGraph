package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * A factory of the characteristic that computes module abstractness.
 *
 * @author Alik
 *
 * @see AbstractnessCharacteristic
 * @see AbstractnessCharacteristicFactoryDescriptor
 */
@AbstractnessCharacteristicFactoryDescriptor
public final class AbstractnessCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AbstractnessCharacteristic, Factor, SingleModuleCharacteristicParameters> {

    @Nonnull
    @Override
    public AbstractnessCharacteristic createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new AbstractnessCharacteristic(this.characteristicId, parameters);
    }
}
