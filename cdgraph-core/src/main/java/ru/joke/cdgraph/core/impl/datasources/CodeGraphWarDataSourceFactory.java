package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public final class CodeGraphWarDataSourceFactory implements CodeGraphDataSourceFactory {

    private final CodeGraphDataSourceFactory nestedJarDataSourceFactory;

    public CodeGraphWarDataSourceFactory(@Nonnull CodeGraphDataSourceFactory nestedJarDataSourceFactory) {
        this.nestedJarDataSourceFactory = nestedJarDataSourceFactory;
    }

    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath) {
        return create(dataPath, new JarClassesMetadataReader());
    }

    @Nonnull
    @Override
    public CodeGraphDataSource create(@Nonnull Path dataPath, @Nonnull ClassesMetadataReader metadataReader) {
        return new CodeGraphWarDataSource(dataPath, metadataReader, this.nestedJarDataSourceFactory);
    }
}
