package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public record ClassMetadata(@Nonnull String className, @Nonnull ClassType classType, @Nonnull String packageName) {

    public enum ClassType {

        INTERFACE,

        ABSTRACT,

        ENUM,

        ANNOTATION,

        CONCRETE;

        public boolean isConcrete() {
            return this == CONCRETE;
        }
    }
}
