package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public final class CodeGraphDataSourceException extends RuntimeException {

    public CodeGraphDataSourceException(@Nonnull Throwable ex) {
        super(ex);
    }
}
