package ru.joke.cdgraph.core.graph.impl;

import ru.joke.cdgraph.core.graph.GraphTag;

import javax.annotation.Nonnull;

public record SimpleGraphTag<T>(@Nonnull String name, @Nonnull T value) implements GraphTag<T> {
}
