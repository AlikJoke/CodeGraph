package ru.joke.cdgraph.core.datasources.impl;

import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Data source implementation for the WAR archives.<br>
 * A factory can be used to create: {@link CodeGraphWarDataSourceFactory}.
 *
 * @author Alik
 * @see CodeGraphDataSource
 */
public class CodeGraphWarDataSource extends CodeGraphFatJarDataSource {

    private static final String WEB_INF_DIR = "WEB-INF/";
    private static final String WAR_DATA_DIR = WEB_INF_DIR + "classes/";
    private static final String WAR_LIB_DIR = WEB_INF_DIR + "lib/";

    public CodeGraphWarDataSource(
            @Nonnull Path dataSourcePath,
            @Nonnull ClassesMetadataReader<JarFile> warMetadataReader,
            @Nonnull CodeGraphDataSourceFactory<JarFile> jarDataSourceFactory) {
        super(dataSourcePath, warMetadataReader, jarDataSourceFactory);
    }

    @Override
    public String toString() {
        return "CodeGraphWarDataSource{" + "dataSourcePath=" + dataSourcePath + '}';
    }

    @Nonnull
    @Override
    protected String getEntryName(@Nonnull JarEntry entry) {
        final var entryName = entry.getName();
        return entryName.startsWith(WAR_DATA_DIR)
                ? entryName.substring(WAR_DATA_DIR.length())
                : entryName;
    }

    protected boolean isNestedJarFile(@Nonnull JarEntry entry) {
        return entry.getName().startsWith(WAR_LIB_DIR)
                && entry.getName().endsWith(JAR_EXTENSION);
    }
}
