package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes graph clusters.
 *
 * @author Alik
 *
 * @see ModuleClusteringCharacteristic
 * @see ModuleClusteringCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("module.clustering")
public @interface ModuleClusteringCharacteristicFactoryHandle {
}
