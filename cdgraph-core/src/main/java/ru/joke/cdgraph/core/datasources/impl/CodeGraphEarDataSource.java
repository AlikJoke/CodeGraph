package ru.joke.cdgraph.core.datasources.impl;

import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Data source implementation for the EAR archives.<br>
 * A factory can be used to create: {@link CodeGraphEarDataSourceFactory}.
 *
 * @author Alik
 * @see CodeGraphDataSource
 */
public final class CodeGraphEarDataSource extends CodeGraphWarDataSource {

    private static final String WAR_EXTENSION = ".war";

    public CodeGraphEarDataSource(
            @Nonnull Path dataSourcePath,
            @Nonnull ClassesMetadataReader<JarFile> earMetadataReader,
            @Nonnull CodeGraphDataSourceFactory<JarFile> warDataSourceFactory) {
        super(dataSourcePath, earMetadataReader, warDataSourceFactory);
    }

    @Override
    public String toString() {
        return "CodeGraphEarDataSource{" + "dataSourcePath=" + dataSourcePath + '}';
    }

    @Nonnull
    @Override
    protected String getEntryName(@Nonnull JarEntry entry) {
        return entry.getName();
    }

    protected boolean isNestedJarFile(@Nonnull JarEntry entry) {
        return entry.getName().endsWith(WAR_EXTENSION) || entry.getName().endsWith(CodeGraphFatJarDataSource.JAR_EXTENSION);
    }
}
