package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes stability of all
 * modules in the graph.
 *
 * @author Alik
 *
 * @see AllModulesStabilityCharacteristic
 * @see AllModulesStabilityCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(characteristicId = "all.modules.stability")
public @interface AllModulesStabilityCharacteristicFactoryDescriptor {
}
