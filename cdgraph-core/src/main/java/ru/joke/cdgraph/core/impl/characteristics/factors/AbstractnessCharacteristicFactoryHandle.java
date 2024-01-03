package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes module abstractness.
 *
 * @author Alik
 *
 * @see AbstractnessCharacteristic
 * @see AbstractnessCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("module.abstractness")
public @interface AbstractnessCharacteristicFactoryHandle {
}
