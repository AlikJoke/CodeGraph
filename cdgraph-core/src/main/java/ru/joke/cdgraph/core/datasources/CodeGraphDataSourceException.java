package ru.joke.cdgraph.core.datasources;

import javax.annotation.Nonnull;

/**
 * An exception describing some problem with the data source during the process of
 * retrieving the configuration required for building a modules graph.
 *
 * @author Alik
 */
public final class CodeGraphDataSourceException extends RuntimeException {

    public CodeGraphDataSourceException(@Nonnull String message) {
        super(message);
    }

    public CodeGraphDataSourceException(@Nonnull Throwable ex) {
        super(ex);
    }
}
