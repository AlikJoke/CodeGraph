package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes conflicting dependencies
 *  * of the module in the code graph.
 *
 * @author Alik
 *
 * @see ConflictingDependenciesCharacteristic
 * @see ConflictingDependenciesCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("conflicting.dependencies")
public @interface ConflictingDependenciesCharacteristicFactoryHandle {
}
