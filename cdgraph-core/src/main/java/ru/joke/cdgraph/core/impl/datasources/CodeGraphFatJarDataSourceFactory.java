package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public final class CodeGraphFatJarDataSourceFactory implements CodeGraphDataSourceFactory {

    private final CodeGraphDataSourceFactory nestedJarDataSourceFactory;

    public CodeGraphFatJarDataSourceFactory() {
        this(new CodeGraphJarDataSourceFactory());
    }

    public CodeGraphFatJarDataSourceFactory(@Nonnull CodeGraphDataSourceFactory nestedJarDataSourceFactory) {
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
        return new CodeGraphFatJarDataSource(dataPath, metadataReader, this.nestedJarDataSourceFactory);
    }
}
