package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes shortest path between
 * two modules of the graph.
 *
 * @author Alik
 *
 * @see ShortestPathBetweenModulesCharacteristic
 * @see ShortestPathBetweenModulesCharacteristicFactoryHandle
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("shortest.path.between.modules")
public @interface ShortestPathBetweenModulesCharacteristicFactoryHandle {
}
