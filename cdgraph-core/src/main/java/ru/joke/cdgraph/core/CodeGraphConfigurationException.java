package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * An exception describing some problem with the building of the modules graph.
 *
 * @author Alik
 */
public final class CodeGraphConfigurationException extends RuntimeException {

    public CodeGraphConfigurationException(@Nonnull Exception cause) {
        super(cause);
    }

    public CodeGraphConfigurationException(@Nonnull String message) {
        super(message);
    }
}
