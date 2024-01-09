package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;

/**
 * An exception thrown when a characteristic's description is not found by the loader.
 *
 * @author Alik
 */
public final class CodeGraphCharacteristicDescriptionNotFoundException extends RuntimeException {

    public CodeGraphCharacteristicDescriptionNotFoundException(@Nonnull String id) {
        super("Characteristic's description not found by id: " + id);
    }
}
