package ru.joke.cdgraph.core.impl.jms;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphFactory;

import javax.annotation.Nonnull;

public final class JavaModuleCodeGraphFactory implements CodeGraphFactory {
    @Nonnull
    @Override
    public CodeGraph create(@Nonnull CodeGraphDataSource dataSource) {
        return new JavaModuleCodeGraph(dataSource);
    }
}
