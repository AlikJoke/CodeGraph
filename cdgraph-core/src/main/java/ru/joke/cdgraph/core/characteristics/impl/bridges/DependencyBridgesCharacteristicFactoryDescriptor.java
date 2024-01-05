package ru.joke.cdgraph.core.characteristics.impl.bridges;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes graph bridges.
 *
 * @author Alik
 *
 * @see DependencyBridgesCharacteristic
 * @see DependencyBridgesCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(characteristicId = "dependency.bridges")
public @interface DependencyBridgesCharacteristicFactoryDescriptor {
}
