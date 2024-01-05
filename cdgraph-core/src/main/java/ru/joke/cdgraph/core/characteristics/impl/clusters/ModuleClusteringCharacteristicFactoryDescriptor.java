package ru.joke.cdgraph.core.characteristics.impl.clusters;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes graph clusters.
 *
 * @author Alik
 *
 * @see ModuleClusteringCharacteristic
 * @see ModuleClusteringCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(
        characteristicId = "module.clustering",
        characteristicParametersType = ModuleClusteringCharacteristicParameters.class
)
public @interface ModuleClusteringCharacteristicFactoryDescriptor {
}
