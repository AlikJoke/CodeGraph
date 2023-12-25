package ru.joke.cdgraph.core.impl.characteristics.counters;

import javax.annotation.Nonnegative;

public record DependenciesCount(@Nonnegative int input, @Nonnegative int output) {
}
