package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public interface CodeGraphDataSource {

    @Nonnull
    String id();

    @Nonnull
    List<Configuration> find(@Nonnull Predicate<String> descriptorFileFilter);

    record Configuration(@Nonnull File descriptor, @Nonnull Set<ClassMetadata> classesMetadata) {
    }
}
