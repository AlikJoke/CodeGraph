package ru.joke.cdgraph.core.impl.jms;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphFactory;

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
