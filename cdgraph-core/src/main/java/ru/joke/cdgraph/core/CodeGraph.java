package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;

public interface CodeGraph {

    @Nonnull
    GraphNode findRootNode();

    @Nonnull
    Optional<GraphNode> findNodeById(@Nonnull String id);

    @Nonnull
    Collection<GraphNode> findAllNodes();
}
