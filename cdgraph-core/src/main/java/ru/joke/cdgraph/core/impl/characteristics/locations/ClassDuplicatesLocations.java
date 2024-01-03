package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Location of duplicated classes in modules graph.
 *
 * @param classQualifiedName fully qualified class name, can not be {@code null}.
 * @param modules modules that contain duplicates of a class, can not be {@code null}.
 */
public record ClassDuplicatesLocations(@Nonnull String classQualifiedName, @Nonnull Set<GraphNode> modules) {
}
