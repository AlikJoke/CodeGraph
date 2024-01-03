package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes graph description in the form
 * of all transitive chains from the root node to all leaves.
 *
 * @author Alik
 *
 * @see CodeGraphDescriptionCharacteristic
 * @see CodeGraphDescriptionCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("graph.description")
public @interface CodeGraphDescriptionCharacteristicFactoryHandle {
}
