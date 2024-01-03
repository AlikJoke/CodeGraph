package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes abstractness of all
 * modules in the graph.
 *
 * @author Alik
 *
 * @see AllModulesAbstractnessCharacteristic
 * @see AllModulesAbstractnessCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("all.modules.abstractness")
public @interface AllModulesAbstractnessCharacteristicFactoryHandle {
}
