package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * An exception describing a problem with writing characteristics calculated
 * on the graph to the configured sink.
 *
 * @author Alik
 */
public final class CodeGraphSinkException extends RuntimeException {

    public CodeGraphSinkException(@Nonnull Exception cause) {
        super(cause);
    }
}
