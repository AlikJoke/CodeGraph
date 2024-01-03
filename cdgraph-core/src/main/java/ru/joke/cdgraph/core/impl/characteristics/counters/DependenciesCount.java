package ru.joke.cdgraph.core.impl.characteristics.counters;

import javax.annotation.Nonnegative;

/**
 * Module dependencies count.
 *
 * @param input count of input dependencies (relations to this module from another)
 * @param output count of output dependencies (relations from this module to another)
 */
public record DependenciesCount(@Nonnegative int input, @Nonnegative int output) {
}
