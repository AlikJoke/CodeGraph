package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes module stability.
 *
 * @author Alik
 *
 * @see StabilityCharacteristic
 * @see StabilityCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("module.stability")
public @interface StabilityCharacteristicFactoryHandle {
}
