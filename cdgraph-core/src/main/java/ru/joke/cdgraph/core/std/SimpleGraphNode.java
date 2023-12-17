package ru.joke.cdgraph.core.std;

import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.GraphTag;

import javax.annotation.Nonnull;
import java.util.Set;

public record SimpleGraphNode(
        @Nonnull String id,
        @Nonnull Set<GraphNodeRelation> dependencies,
        @Nonnull Set<GraphTag> tags) implements GraphNode {

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
