package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public interface CodeGraphDataSourceFactory<T> {

    @Nonnull
    CodeGraphDataSource create(@Nonnull Path dataPath);

    @Nonnull
    CodeGraphDataSource create(@Nonnull Path dataPath, @Nonnull ClassesMetadataReader<T> metadataReader);
}
