package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.jar.JarFile;

public interface ClassesMetadataReader {

    @Nonnull
    Set<ClassMetadata> read(@Nonnull JarFile jar);
}
