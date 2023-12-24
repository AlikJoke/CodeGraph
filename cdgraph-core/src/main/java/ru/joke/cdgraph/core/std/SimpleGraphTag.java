package ru.joke.cdgraph.core.std;

import ru.joke.cdgraph.core.GraphTag;

import javax.annotation.Nonnull;

public record SimpleGraphTag<T>(@Nonnull String name, @Nonnull T value) implements GraphTag<T> {
}
