package ru.joke.cdgraph.core.std.characteristics.counters;

import javax.annotation.Nonnegative;

public record DependenciesCount(@Nonnegative int input, @Nonnegative int output) {
}
