package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.graph.GraphNode;

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
