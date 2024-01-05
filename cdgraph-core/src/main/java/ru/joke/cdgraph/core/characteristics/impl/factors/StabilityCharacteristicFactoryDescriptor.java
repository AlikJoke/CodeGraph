package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes module stability.
 *
 * @author Alik
 *
 * @see StabilityCharacteristic
 * @see StabilityCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(
        characteristicId = "module.stability",
        characteristicParametersType = SingleModuleCharacteristicParameters.class
)
public @interface StabilityCharacteristicFactoryDescriptor {
}
