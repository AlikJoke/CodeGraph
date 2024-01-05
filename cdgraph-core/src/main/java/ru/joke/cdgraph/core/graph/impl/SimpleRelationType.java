package ru.joke.cdgraph.core.graph.impl;

import ru.joke.cdgraph.core.graph.GraphNodeRelation;

import javax.annotation.Nonnull;

public record SimpleRelationType(@Nonnull String name, boolean isTransitive) implements GraphNodeRelation.RelationType {

    public SimpleRelationType(@Nonnull String name) {
        this(name, true);
    }
}
