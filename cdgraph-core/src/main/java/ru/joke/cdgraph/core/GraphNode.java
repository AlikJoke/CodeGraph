package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Set;

public interface GraphNode {

    @Nonnull
    String id();

    @Nonnull
    Set<GraphNodeRelation> relations();

    @Nonnull
    Set<GraphTag> tags();
}
