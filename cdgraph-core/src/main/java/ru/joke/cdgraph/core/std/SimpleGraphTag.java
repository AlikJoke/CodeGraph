package ru.joke.cdgraph.core.std;

import ru.joke.cdgraph.core.GraphTag;

import javax.annotation.Nonnull;

public record SimpleGraphTag(@Nonnull String name, @Nonnull Object value) implements GraphTag {
}
