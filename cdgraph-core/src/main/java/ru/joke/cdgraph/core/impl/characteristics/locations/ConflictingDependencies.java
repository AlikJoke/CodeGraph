package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Conflicting dependencies. Contains modules from graph that conflict with each other.
 *
 * @param conflictingDependencies modules from graph that conflict with each other,
 *                                can not be {@code null}.
 */
public record ConflictingDependencies(@Nonnull Set<GraphNode> conflictingDependencies) {
}
