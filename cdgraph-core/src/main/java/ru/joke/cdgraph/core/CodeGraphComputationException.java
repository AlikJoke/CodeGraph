package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * An exception describing some problem during the computation of the characteristic.
 *
 * @author Alik
 */
public final class CodeGraphComputationException extends RuntimeException {

    public CodeGraphComputationException(@Nonnull final String message) {
        super(message);
    }
}
