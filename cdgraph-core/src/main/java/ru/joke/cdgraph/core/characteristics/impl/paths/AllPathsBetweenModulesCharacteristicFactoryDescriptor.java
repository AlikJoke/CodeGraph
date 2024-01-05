package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes all paths between
 * two modules of the graph.
 *
 * @author Alik
 *
 * @see AllPathsBetweenModulesCharacteristic
 * @see AllPathsBetweenModulesCharacteristicFactoryDescriptor
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(
        characteristicId = "all.paths.between.modules",
        characteristicParametersType = PathBetweenModulesCharacteristicParameters.class
)
public @interface AllPathsBetweenModulesCharacteristicFactoryDescriptor {
}
