package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes all paths between
 * two modules of the graph.
 *
 * @author Alik
 *
 * @see AllPathsBetweenModulesCharacteristic
 * @see AllPathsBetweenModulesCharacteristicFactoryHandle
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("all.paths.between.modules")
public @interface AllPathsBetweenModulesCharacteristicFactoryHandle {
}
