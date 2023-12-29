package ru.joke.cdgraph.core.impl.characteristics.clusters;

import javax.annotation.Nonnull;

public record ClusterRelation(@Nonnull Cluster source, @Nonnull Cluster target) {
}
