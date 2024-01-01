package ru.joke.cdgraph.core.impl.maven;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphFactory;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;

public final class MavenModuleCodeGraphFactory implements CodeGraphFactory {
    @Nonnull
    @Override
    public CodeGraph create(@Nonnull CodeGraphDataSource dataSource) {
        return new MavenModuleCodeGraph(dataSource, new JarClassesMetadataReader());
    }
}
