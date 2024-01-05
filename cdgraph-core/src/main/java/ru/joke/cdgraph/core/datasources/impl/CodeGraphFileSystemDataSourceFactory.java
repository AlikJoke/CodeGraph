package ru.joke.cdgraph.core.datasources.impl;

import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.meta.impl.FSDirectoryClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;

/**
 * Factory of the {@link CodeGraphFileSystemDataSource}.
 *
 * @author Alik
 * @see CodeGraphDataSourceFactory
 */
public final class CodeGraphFileSystemDataSourceFactory implements CodeGraphDataSourceFactory<Path> {
    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath) {
        return create(dataPath, new FSDirectoryClassesMetadataReader());
    }

    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath, @Nonnull ClassesMetadataReader<Path> metadataReader) {
        return new CodeGraphFileSystemDataSource(dataPath, metadataReader);
    }
}
