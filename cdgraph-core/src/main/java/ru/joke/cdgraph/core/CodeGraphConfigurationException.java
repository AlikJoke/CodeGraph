package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public final class CodeGraphConfigurationException extends RuntimeException {

    public CodeGraphConfigurationException(@Nonnull String message) {
        super(message);
    }
}