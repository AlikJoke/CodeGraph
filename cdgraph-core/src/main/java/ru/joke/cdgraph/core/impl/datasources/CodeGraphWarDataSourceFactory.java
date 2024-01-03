package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.impl.meta.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarFile;

/**
 * Factory of the WAR data source ({@link CodeGraphWarDataSource}).
 *
 * @author Alik
 * @see CodeGraphDataSourceFactory
 */
public final class CodeGraphWarDataSourceFactory implements CodeGraphDataSourceFactory<JarFile> {

    private final CodeGraphDataSourceFactory<JarFile> nestedJarDataSourceFactory;

    public CodeGraphWarDataSourceFactory() {
        this(new CodeGraphJarDataSourceFactory());
    }

    public CodeGraphWarDataSourceFactory(@Nonnull CodeGraphDataSourceFactory<JarFile> nestedJarDataSourceFactory) {
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
        return new CodeGraphWarDataSource(dataPath, metadataReader, this.nestedJarDataSourceFactory);
    }
}
