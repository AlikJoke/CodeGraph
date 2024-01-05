package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes locations
 * of the package with specified name.
 *
 * @author Alik
 *
 * @see PackageLocationsCharacteristic
 * @see PackageLocationsCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(
        characteristicId = "package.locations",
        characteristicParametersType = ResourceLocationsCharacteristicParameters.class
)
public @interface PackageLocationsCharacteristicFactoryDescriptor {
}
