package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes duplicates of classes between
 * different graph modules.
 *
 * @author Alik
 *
 * @see AllClassesDuplicatesLocationsCharacteristic
 * @see AllClassesDuplicatesLocationsCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("all.classes.duplicates.locations")
public @interface AllClassesDuplicatesLocationsCharacteristicFactoryHandle {
}
