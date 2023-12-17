package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface GraphTag {

    @Nonnull
    String name();

    @Nonnull
    Object value();
}
