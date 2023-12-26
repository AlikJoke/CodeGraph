package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

public record ConflictingDependencies(@Nonnull Set<GraphNode> conflictingDependencies) {
}
