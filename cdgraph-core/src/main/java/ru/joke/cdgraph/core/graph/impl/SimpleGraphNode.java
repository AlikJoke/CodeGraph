package ru.joke.cdgraph.core.graph.impl;

import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.graph.GraphTag;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public record SimpleGraphNode(
        @Nonnull String id,
        @Nonnull Set<GraphNodeRelation> relations,
        @Nonnull Map<String, GraphTag<?>> tags) implements GraphNode {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SimpleGraphNode that = (SimpleGraphNode) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
