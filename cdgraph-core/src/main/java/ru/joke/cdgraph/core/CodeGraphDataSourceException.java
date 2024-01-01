package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public final class CodeGraphDataSourceException extends RuntimeException {

    public CodeGraphDataSourceException(@Nonnull String message) {
        super(message);
    }

    public CodeGraphDataSourceException(@Nonnull Throwable ex) {
        super(ex);
    }
}
