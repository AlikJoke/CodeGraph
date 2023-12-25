package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public record ClassMetadata(@Nonnull String className, @Nonnull ClassType classType, @Nonnull String packageName) {

    public enum ClassType {

        INTERFACE,

        ABSTRACT,

        ANNOTATION,

        CONCRETE;

        public boolean isConcrete() {
            return this == CONCRETE;
        }
    }
}
