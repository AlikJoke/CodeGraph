package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * An exception thrown when a characteristic factory is not found in the registry.
 *
 * @author Alik
 */
public final class CodeGraphCharacteristicFactoryNotFoundException extends RuntimeException {

    public CodeGraphCharacteristicFactoryNotFoundException(@Nonnull String id) {
        super("Characteristic factory not found by id: " + id);
    }
}
