package ru.joke.cdgraph.starter.shared;

import javax.annotation.Nonnull;

/**
 * Types of the output sinks.
 *
 * @author Alik
 * @see ru.joke.cdgraph.core.client.CodeGraphOutputSink
 */
public enum CodeGraphOutputSinkType {

    /**
     * File sink
     */
    FILE("file"),

    /**
     * Console sink
     */
    CONSOLE("console");

    private final String alias;

    CodeGraphOutputSinkType(@Nonnull String alias) {
        this.alias = alias;
    }

    /**
     * Returns the alias of the sink.
     * @return alias of the sink, can not be {@code null}.
     */
    @Nonnull
    public String getAlias() {
        return this.alias;
    }

    /**
     * Converts string alias to the enum value.
     *
     * @param alias alias of the enum value, can not be {@code null}.
     * @return enum value, can not be {@code null}.
     */
    @Nonnull
    public static CodeGraphOutputSinkType from(@Nonnull String alias) {
        if (FILE.alias.equalsIgnoreCase(alias)) {
            return FILE;
        } else if (CONSOLE.alias.equalsIgnoreCase(alias)) {
            return CONSOLE;
        } else {
            throw new IllegalArgumentException("Unsupported type of output sink: " + alias);
        }
    }
}
