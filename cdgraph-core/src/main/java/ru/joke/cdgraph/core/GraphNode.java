package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

/**
 * Representation of vertices (nodes) of a graph. In a {@link CodeGraph}, the equivalent is the
 * module descriptor. Contains relations with other graph nodes ({@linkplain GraphNode#relations()}
 * and a set of tags ({@linkplain GraphNode#tags()} containing additional module data.
 *
 * @author Alik
 *
 * @see CodeGraph
 * @see GraphNodeRelation
 * @see GraphTag
 */
public interface GraphNode {

    /**
     * Returns the id (unique name in the graph) of the node (module).
     * @return cannot be {@code null} or empty.
     */
    @Nonnull
    String id();

    /**
     * Returns the relations of the node to other graph nodes.
     * @return cannot be {@code null}, but can be empty.
     *
     * @see GraphNodeRelation
     */
    @Nonnull
    Set<GraphNodeRelation> relations();

    /**
     * Returns a map of tags containing additional data about the node (module).
     * @return cannot be {@code null}, but can be empty.
     *
     * @see GraphTag
     */
    @Nonnull
    Map<String, GraphTag<?>> tags();
}
