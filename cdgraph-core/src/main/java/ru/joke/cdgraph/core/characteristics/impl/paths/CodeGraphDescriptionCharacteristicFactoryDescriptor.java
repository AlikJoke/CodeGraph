package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes graph description in the form
 * of all transitive chains from the root node to all leaves.
 *
 * @author Alik
 *
 * @see CodeGraphDescriptionCharacteristic
 * @see CodeGraphDescriptionCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(characteristicId = "graph.description")
public @interface CodeGraphDescriptionCharacteristicFactoryDescriptor {
}
