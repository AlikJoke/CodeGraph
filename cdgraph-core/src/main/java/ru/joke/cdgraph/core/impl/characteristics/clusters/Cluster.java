package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

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

    public int computeOutputEdgesCount() {
        return relations.size();
    }

    public int computeExternalInputEdgesCount(final Set<Cluster> clusters) {
        int edgesCount = 0;
        for (var cluster : clusters) {

            for (var relation : cluster.relations()) {
                edgesCount += this == relation.target() ? 1 : 0;
            }
        }

        return edgesCount;
    }
}
