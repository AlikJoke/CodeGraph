package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.function.Predicate;

public interface CodeGraphDataSource {

    @Nonnull
    List<File> find(@Nonnull Predicate<String> filter);
}
