package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface GraphTag<T> {

    @Nonnull
    String name();

    @Nonnull
    T value();
}
