package ru.joke.cdgraph.core.graph.impl.jpms;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.graph.CodeGraphFactory;

import javax.annotation.Nonnull;

/**
 * Implementation of a factory for creating a modules graph based on Java modules.
 *
 * @author Alik
 * @see CodeGraphFactory
 * @see CodeGraph
 */
public final class JavaModuleCodeGraphFactory implements CodeGraphFactory {
    @Nonnull
    @Override
    public CodeGraph create(@Nonnull CodeGraphDataSource dataSource) {
        return new JavaModuleCodeGraph(dataSource);
    }
}
