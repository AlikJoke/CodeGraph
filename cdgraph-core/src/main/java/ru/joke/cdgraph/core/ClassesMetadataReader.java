package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Set;

public interface ClassesMetadataReader<T> {

    @Nonnull
    Set<ClassMetadata> read(@Nonnull T source);
}
