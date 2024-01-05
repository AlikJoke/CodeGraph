package ru.joke.cdgraph.starter.shared;

import javax.annotation.Nonnull;

/**
 * Types of the {@link ru.joke.cdgraph.core.graph.CodeGraph}.
 *
 * @author Alik
 * @see ru.joke.cdgraph.core.graph.CodeGraph
 */
public enum CodeGraphType {

    /**
     * Maven modules graph
     */
    MAVEN_MODULES("maven-modules"),

    /**
     * Java modules graph
     */
    JAVA_MODULES("java-modules");

    private final String alias;

    CodeGraphType(@Nonnull String alias) {
        this.alias = alias;
    }

    /**
     * Returns the alias of the graph type.
     * @return the alias of the graph type, can not be {@code null}.
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
    public static CodeGraphType from(@Nonnull String alias) {
        if (MAVEN_MODULES.alias.equalsIgnoreCase(alias)) {
            return MAVEN_MODULES;
        } else if (JAVA_MODULES.alias.equalsIgnoreCase(alias)) {
            return JAVA_MODULES;
        } else {
            throw new IllegalArgumentException("Unsupported type of graph: " + alias);
        }
    }
}
