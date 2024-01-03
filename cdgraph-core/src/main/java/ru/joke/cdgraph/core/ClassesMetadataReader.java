package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Code graph module classes metadata reader.
 *
 * @param <T> type of the classes source (JAR or directory)
 *
 * @author Alik
 * @see ClassMetadata
 */
public interface ClassesMetadataReader<T> {

    /**
     * Reads the classes metadata from the source.
     *
     * @param source source of the classes, can not be {@code null}.
     * @return classes metadata, can not be {@code null}.
     *
     * @see ClassMetadata
     */
    @Nonnull
    Set<ClassMetadata> read(@Nonnull T source);
}
