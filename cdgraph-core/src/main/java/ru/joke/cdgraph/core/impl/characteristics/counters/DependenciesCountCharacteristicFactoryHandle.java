package ru.joke.cdgraph.core.impl.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes module dependencies count.
 *
 * @author Alik
 *
 * @see DependenciesCountCharacteristic
 * @see DependenciesCountCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("dependencies.count")
public @interface DependenciesCountCharacteristicFactoryHandle {
}
