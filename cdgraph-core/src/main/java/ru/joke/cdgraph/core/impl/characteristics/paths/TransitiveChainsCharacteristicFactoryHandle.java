package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A handle of the factory of the characteristic that computes all transitive chains (paths)
 * from the specified module node.
 *
 * @author Alik
 *
 * @see TransitiveChainsCharacteristic
 * @see TransitiveChainsCharacteristicFactoryHandle
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("transitive.chains")
public @interface TransitiveChainsCharacteristicFactoryHandle {
}
