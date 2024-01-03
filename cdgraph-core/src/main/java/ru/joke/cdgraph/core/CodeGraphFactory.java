package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * Factory for creating instances of code graphs based on a given module code storage source.<br>
 * Depending on the type of graph (from which modules it is built), the implementation of
 * the factory will be different.
 *
 * @author Alik
 *
 * @see CodeGraph
 * @see CodeGraphDataSource
 */
public interface CodeGraphFactory {

    /**
     * Creates a modules graph based on the passed data source.
     * @param dataSource data source from which the graph is constructed, can not be {@code null}.
     * @return modules graph, can not be {@code null}.
     */
    @Nonnull
    CodeGraph create(@Nonnull CodeGraphDataSource dataSource);
}
