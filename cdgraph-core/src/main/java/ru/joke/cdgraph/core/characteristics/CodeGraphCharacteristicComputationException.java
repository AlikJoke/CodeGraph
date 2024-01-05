package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;

/**
 * An exception describing some problem during the computation of the characteristic.
 *
 * @author Alik
 */
public final class CodeGraphCharacteristicComputationException extends RuntimeException {

    public CodeGraphCharacteristicComputationException(@Nonnull final String message) {
        super(message);
    }
}
