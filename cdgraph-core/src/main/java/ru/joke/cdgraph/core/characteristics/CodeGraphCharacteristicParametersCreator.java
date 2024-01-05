package ru.joke.cdgraph.core.characteristics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Descriptor of the constructors-factories of the characteristic parameters.
 *
 * @author Alik
 * @see CodeGraphCharacteristicParameter
 */
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeGraphCharacteristicParametersCreator {
}
