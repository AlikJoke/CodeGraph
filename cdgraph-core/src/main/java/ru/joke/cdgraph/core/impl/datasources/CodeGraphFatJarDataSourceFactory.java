package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.impl.meta.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarFile;

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
