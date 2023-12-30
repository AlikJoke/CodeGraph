package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphFactory {

    @Nonnull
    CodeGraph create(@Nonnull CodeGraphDataSource dataSource);
}
