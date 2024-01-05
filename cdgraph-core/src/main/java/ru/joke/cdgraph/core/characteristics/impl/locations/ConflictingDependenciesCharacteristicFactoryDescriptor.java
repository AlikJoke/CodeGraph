package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes conflicting dependencies
 *  * of the module in the code graph.
 *
 * @author Alik
 *
 * @see ConflictingDependenciesCharacteristic
 * @see ConflictingDependenciesCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(characteristicId = "conflicting.dependencies")
public @interface ConflictingDependenciesCharacteristicFactoryDescriptor {
}
