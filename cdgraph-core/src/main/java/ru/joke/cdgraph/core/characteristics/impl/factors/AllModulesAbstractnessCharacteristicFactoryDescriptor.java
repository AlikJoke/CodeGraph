package ru.joke.cdgraph.core.characteristics.impl.factors;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes abstractness of all
 * modules in the graph.
 *
 * @author Alik
 *
 * @see AllModulesAbstractnessCharacteristic
 * @see AllModulesAbstractnessCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(characteristicId = "all.modules.abstractness")
public @interface AllModulesAbstractnessCharacteristicFactoryDescriptor {
}
