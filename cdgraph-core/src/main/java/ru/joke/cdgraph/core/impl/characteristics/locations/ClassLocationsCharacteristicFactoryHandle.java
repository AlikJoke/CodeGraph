package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes locations
 * of the class with specified name.
 *
 * @author Alik
 *
 * @see ClassLocationsCharacteristicFactory
 * @see ClassLocationsCharacteristic
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("class.locations")
public @interface ClassLocationsCharacteristicFactoryHandle {
}
