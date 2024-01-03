package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.impl.meta.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarFile;

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
