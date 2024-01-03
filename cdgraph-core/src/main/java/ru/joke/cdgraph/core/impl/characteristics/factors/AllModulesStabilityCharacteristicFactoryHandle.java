package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes stability of all
 * modules in the graph.
 *
 * @author Alik
 *
 * @see AllModulesStabilityCharacteristic
 * @see AllModulesStabilityCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("all.modules.stability")
public @interface AllModulesStabilityCharacteristicFactoryHandle {
}
