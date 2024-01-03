package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

/**
 * The central abstraction of the application. It is a graph whose nodes are modules (module descriptors),
 * and the edges between nodes are dependencies between code modules.<br>
 *
 * The following applies to the CodeGraph:
 * <ul>
 * <li>The code modules graph is always an acyclic directed graph (DAG), since cyclic dependencies in the code are not allowed;</li>
 * <li>The graph always contains only one root module (without incoming edges);</li>
 * <li>The graph can contain only one node, but cannot be empty.</li>
 * </ul>
 *
 * The graph is the context for calculating various characteristics of the application being analyzed.
 *
 * @author Alik
 *
 * @see GraphNode
 * @see GraphNodeRelation
 */
public interface CodeGraph {

    /**
     * Returns the root vertex (the vertex with no incoming edges).
     * @return the root vertex, cannot be {@code null}.
     *
     * @see GraphNode
     */
    @Nonnull
    GraphNode findRootNode();

    /**
     * Finds a graph node by its unique identifier in the graph.
     * @return a graph node by its unique identifier, cannot be {@code null},
     * but can be {@linkplain Optional#empty()} if node with such identifier does not exist.
     *
     * @see GraphNode
     */
    @Nonnull
    Optional<GraphNode> findNodeById(@Nonnull String id);

    /**
     * Returns a collection of all vertices of the graph (order is not guaranteed).
     * @return a collection of all vertices of the graph, can not be {@code null}.
     *
     * @see GraphNode
     */
    @Nonnull
    Collection<GraphNode> findAllNodes();
}
