package ru.joke.cdgraph.core.graph;

import javax.annotation.Nonnull;

/**
 * A graph node or edge tag that contains additional information about the node or edge.
 *
 * @param <T> - type of the tag value
 * @author Alik
 *
 * @see GraphNodeRelation
 * @see GraphNode
 */
public interface GraphTag<T> {

    /**
     * Returns the name (id) of the tag.
     * @return the name of the tag, can not be {@code null}.
     */
    @Nonnull
    String name();

    /**
     * Returns the value of the tag.
     * @return the value of the tag, can not be {@code null}.
     */
    @Nonnull
    T value();
}
