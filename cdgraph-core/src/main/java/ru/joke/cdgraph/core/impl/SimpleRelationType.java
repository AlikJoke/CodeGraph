package ru.joke.cdgraph.core.impl;

import ru.joke.cdgraph.core.GraphNodeRelation;

import javax.annotation.Nonnull;

public record SimpleRelationType(@Nonnull String name, boolean isTransitive) implements GraphNodeRelation.RelationType {

    public SimpleRelationType(@Nonnull String name) {
        this(name, true);
    }
}
