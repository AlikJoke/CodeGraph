package ru.joke.cdgraph.core.characteristics.impl.clusters;

import ru.joke.cdgraph.core.graph.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Cluster of the linked graph nodes.
 *
 * @param includedNodes included in cluster graph nodes, can not be {@code null}.
 * @param relations relations between this cluster and other clusters, can not be {@code null}.
 *
 * @author Alik
 * @see ClusterRelation
 * @see GraphNode
 */
public record Cluster(
        @Nonnull Set<GraphNode> includedNodes,
        @Nonnull Set<ClusterRelation> relations) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Cluster cluster = (Cluster) o;
        return includedNodes.equals(cluster.includedNodes);
    }

    @Override
    public int hashCode() {
        return includedNodes.hashCode();
    }

    @Override
    public String toString() {
        return "Cluster{" + "includedNodes=" + includedNodes + '}';
    }

    /**
     * Computes the count of the output edges from this cluster.
     * @return the count of the output edges.
     */
    public int computeOutputEdgesCount() {
        return relations.size();
    }

    /**
     * Computes the count of the input edges to this cluster from another clusters.
     * @param clusters linked clusters, can not be {@code null}.
     * @return the count of the input edges.
     */
    public int computeExternalInputEdgesCount(@Nonnull Set<Cluster> clusters) {
        int edgesCount = 0;
        for (var cluster : clusters) {

            for (var relation : cluster.relations()) {
                edgesCount += this == relation.target() ? 1 : 0;
            }
        }

        return edgesCount;
    }
}
