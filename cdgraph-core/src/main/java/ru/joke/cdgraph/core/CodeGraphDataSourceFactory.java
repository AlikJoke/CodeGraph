package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.nio.file.Path;

/**
 * Factory for creating a data source for building a modules graph.
 *
 * @param <T> type of the data (archive (JAR) or directory with compiled sources).
 * @author Alik
 * @see CodeGraphDataSource
 * @see ClassesMetadataReader
 */
public interface CodeGraphDataSourceFactory<T> {

    /**
     * Creates a data source by specified path to data with the default classes metadata reader.
     *
     * @param dataPath path to data, can not be {@code null}.
     * @return a data source from the specified path, can not be {@code null}.
     * @see CodeGraphDataSource
     */
    @Nonnull
    CodeGraphDataSource create(@Nonnull Path dataPath);

    /**
     * Creates a data source by specified path to data with the specified classes metadata reader.
     *
     * @param dataPath path to data, can not be {@code null}.
     * @param metadataReader classes metadata reader, can not be {@code null}.
     * @return a data source from the specified path, can not be {@code null}.
     * @see CodeGraphDataSource
     * @see ClassesMetadataReader
     */
    @Nonnull
    CodeGraphDataSource create(@Nonnull Path dataPath, @Nonnull ClassesMetadataReader<T> metadataReader);
}
