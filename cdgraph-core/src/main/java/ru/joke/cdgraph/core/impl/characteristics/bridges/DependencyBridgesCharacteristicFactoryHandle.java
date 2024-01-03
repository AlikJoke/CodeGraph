package ru.joke.cdgraph.core.impl.characteristics.bridges;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes graph bridges.
 *
 * @author Alik
 *
 * @see DependencyBridgesCharacteristic
 * @see DependencyBridgesCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("dependency.bridges")
public @interface DependencyBridgesCharacteristicFactoryHandle {
}
