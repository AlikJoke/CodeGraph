package ru.joke.cdgraph.core.characteristics.impl.clusters;

import javax.annotation.Nonnull;

/**
 * Relation between clusters of the graph nodes.
 *
 * @param source source cluster, can not be {@code null}.
 * @param target target cluster, can not be {@code null}.
 *
 * @author Alik
 * @see Cluster
 */
public record ClusterRelation(@Nonnull Cluster source, @Nonnull Cluster target) {
}
