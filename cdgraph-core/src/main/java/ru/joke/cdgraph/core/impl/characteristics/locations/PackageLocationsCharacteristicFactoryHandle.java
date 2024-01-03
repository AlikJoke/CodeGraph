package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes locations
 * of the package with specified name.
 *
 * @author Alik
 *
 * @see PackageLocationsCharacteristic
 * @see PackageLocationsCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("package.locations")
public @interface PackageLocationsCharacteristicFactoryHandle {
}
