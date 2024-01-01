package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.jar.JarEntry;

public final class CodeGraphEarDataSource extends CodeGraphWarDataSource {

    private static final String JAR_EXTENSION = ".jar";
    private static final String WAR_EXTENSION = ".war";

    public CodeGraphEarDataSource(
            @Nonnull Path dataSourcePath,
            @Nonnull ClassesMetadataReader earMetadataReader,
            @Nonnull CodeGraphDataSourceFactory warDataSourceFactory) {
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
        return entry.getName().endsWith(WAR_EXTENSION) || entry.getName().endsWith(JAR_EXTENSION);
    }
}
