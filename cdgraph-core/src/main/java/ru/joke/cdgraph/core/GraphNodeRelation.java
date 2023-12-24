package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Set;

public interface GraphNodeRelation {

    @Nonnull
    GraphNode source();

    @Nonnull
    GraphNode target();

    @Nonnull
    String type();

    @Nonnull
    Set<GraphTag<?>> tags();
}
