package ru.joke.cdgraph.core.datasources.impl;

import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarFile;

/**
 * Factory of the FatJar data source ({@link CodeGraphFatJarDataSource}).
 *
 * @author Alik
 * @see CodeGraphDataSourceFactory
 */
public final class CodeGraphFatJarDataSourceFactory implements CodeGraphDataSourceFactory<JarFile> {

    private final CodeGraphDataSourceFactory<JarFile> nestedJarDataSourceFactory;

    public CodeGraphFatJarDataSourceFactory() {
        this(new CodeGraphJarDataSourceFactory());
    }

    public CodeGraphFatJarDataSourceFactory(@Nonnull CodeGraphDataSourceFactory<JarFile> nestedJarDataSourceFactory) {
        this.nestedJarDataSourceFactory = nestedJarDataSourceFactory;
    }

    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath) {
        return create(dataPath, new JarClassesMetadataReader());
    }

    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath, @Nonnull ClassesMetadataReader<JarFile> metadataReader) {
        return new CodeGraphFatJarDataSource(dataPath, metadataReader, this.nestedJarDataSourceFactory);
    }
}
