package ru.joke.cdgraph.core.impl;

import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.GraphTag;

import javax.annotation.Nonnull;
import java.util.Map;

public record SimpleGraphNodeRelation(
        @Nonnull GraphNode source,
        @Nonnull GraphNode target,
        @Nonnull RelationType type,
        @Nonnull Map<String, GraphTag<?>> tags) implements GraphNodeRelation {

    @Override
    public String toString() {
        return "SimpleGraphNodeRelation{" + "source=" + source.id() + ", target=" + target.id() + ", type='" + type + '\'' + ", tags=" + tags + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SimpleGraphNodeRelation that = (SimpleGraphNodeRelation) o;
        return source.equals(that.source) && target.equals(that.target) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
