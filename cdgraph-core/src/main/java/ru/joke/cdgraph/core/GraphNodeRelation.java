package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Map;

public interface GraphNodeRelation {

    @Nonnull
    GraphNode source();

    @Nonnull
    GraphNode target();

    @Nonnull
    RelationType type();

    @Nonnull
    Map<String, GraphTag<?>> tags();

    enum RelationType {

        REQUIRES,

        PROVIDED,

        COMPILE,

        RUNTIME,

        SYSTEM
    }
}
