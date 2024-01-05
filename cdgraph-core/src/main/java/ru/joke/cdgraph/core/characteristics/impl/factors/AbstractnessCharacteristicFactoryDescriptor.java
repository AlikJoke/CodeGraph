package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes module abstractness.
 *
 * @author Alik
 *
 * @see AbstractnessCharacteristic
 * @see AbstractnessCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(
        characteristicId = "module.abstractness",
        characteristicParametersType = SingleModuleCharacteristicParameters.class
)
public @interface AbstractnessCharacteristicFactoryDescriptor {
}
