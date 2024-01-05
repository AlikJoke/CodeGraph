package ru.joke.cdgraph.core.graph;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Representation of an edge between nodes (vertices; {@link GraphNode}) of a graph. In a code modules graph, each edge
 * represents a module's dependency on another module. Each edge is directed in one direction,
 * from the source module to the dependent module. Edges in the graph have a specific type
 * and can contain additional data about the connection between modules in the form of tags ({@link GraphTag}.
 *
 * @author Alik
 *
 * @see GraphNode
 * @see GraphTag
 */
public interface GraphNodeRelation {

    /**
     * Returns the source node of this edge.
     * @return the source node of this edge, can not be {@code null}.
     *
     * @see GraphNode
     */
    @Nonnull
    GraphNode source();

    /**
     * Returns the target node of this edge.
     * @return the target node of this edge, can not be {@code null}.
     *
     * @see GraphNode
     */
    @Nonnull
    GraphNode target();

    /**
     * Returns the type of the relation (edge).
     * @return the type of the relation, can not be {@code null}.
     *
     * @see RelationType
     */
    @Nonnull
    RelationType type();

    /**
     * Returns a map of tags containing additional data about the edge (relation).
     * @return cannot be {@code null}, but can be empty.
     *
     * @see GraphTag
     */
    @Nonnull
    Map<String, GraphTag<?>> tags();

    /**
     * Type of relation (edge) between nodes (modules).
     *
     * @author Alik
     */
    interface RelationType {

        /**
         * Returns the name of the relation.
         * @return the name of the relation, can not be {@code null}.
         */
        @Nonnull
        String name();

        /**
         * Returns whether the relationship is transitive.
         * @return {@code true} if the relationship is transitive, {@code false} otherwise.
         */
        boolean isTransitive();
    }
}
