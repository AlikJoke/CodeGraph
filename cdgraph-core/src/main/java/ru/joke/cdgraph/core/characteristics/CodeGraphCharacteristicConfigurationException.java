package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;

/**
 * An exception that occurs when a characteristic is configured incorrectly.
 *
 * @author Alik
 */
public final class CodeGraphCharacteristicConfigurationException extends RuntimeException {

    public CodeGraphCharacteristicConfigurationException(@Nonnull final Exception cause) {
        super(cause);
    }

    public CodeGraphCharacteristicConfigurationException(@Nonnull final String message) {
        super(message);
    }
}
