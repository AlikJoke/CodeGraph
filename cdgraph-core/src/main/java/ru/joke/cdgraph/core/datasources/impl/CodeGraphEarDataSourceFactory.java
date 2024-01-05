package ru.joke.cdgraph.core.datasources.impl;

import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarFile;

/**
 * Data source factory implementation for the EAR archives ({@link CodeGraphEarDataSource}).
 *
 * @author Alik
 * @see CodeGraphDataSource
 */
public final class CodeGraphEarDataSourceFactory implements CodeGraphDataSourceFactory<JarFile> {

    private final CodeGraphDataSourceFactory<JarFile> nestedWarDataSourceFactory;

    public CodeGraphEarDataSourceFactory() {
        this(new CodeGraphWarDataSourceFactory());
    }

    public CodeGraphEarDataSourceFactory(@Nonnull CodeGraphDataSourceFactory<JarFile> nestedWarDataSourceFactory) {
        this.nestedWarDataSourceFactory = nestedWarDataSourceFactory;
    }

    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath) {
        return create(dataPath, new JarClassesMetadataReader());
    }

    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath, @Nonnull ClassesMetadataReader<JarFile> metadataReader) {
        return new CodeGraphEarDataSource(dataPath, metadataReader, this.nestedWarDataSourceFactory);
    }
}
