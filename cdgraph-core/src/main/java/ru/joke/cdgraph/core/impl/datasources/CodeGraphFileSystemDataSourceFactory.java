package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.impl.meta.FSDirectoryClassesMetadataReader;

import javax.annotation.Nonnull;
import java.nio.file.Path;

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
