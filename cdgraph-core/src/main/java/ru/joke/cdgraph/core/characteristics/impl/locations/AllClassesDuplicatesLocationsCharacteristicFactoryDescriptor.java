package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes duplicates of classes between
 * different graph modules.
 *
 * @author Alik
 *
 * @see AllClassesDuplicatesLocationsCharacteristic
 * @see AllClassesDuplicatesLocationsCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(characteristicId = "all.classes.duplicates.locations")
public @interface AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor {
}
