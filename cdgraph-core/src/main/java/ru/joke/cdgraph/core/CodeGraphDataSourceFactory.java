package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.nio.file.Path;

public interface CodeGraphDataSourceFactory {

    @Nonnull
    CodeGraphDataSource create(@Nonnull Path dataPath);
}
