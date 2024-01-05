package ru.joke.cdgraph.starter.shared;

import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.meta.impl.FSDirectoryClassesMetadataReader;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Types of the readers for the classes metadata.
 *
 * @author Alik
 * @see ClassesMetadataReader
 */
public enum ClassesMetadataReaderType {

    /**
     * JAR metadata reader
     */
    JAR("jar", JarClassesMetadataReader::new),

    /**
     * File system directory metadata reader
     */
    DIRECTORY("directory", FSDirectoryClassesMetadataReader::new);

    private final String alias;
    private final Supplier<ClassesMetadataReader<?>> metadataReaderSupplier;

    ClassesMetadataReaderType(
            @Nonnull String alias,
            @Nonnull Supplier<ClassesMetadataReader<?>> metadataReaderSupplier) {
        this.alias = alias;
        this.metadataReaderSupplier = metadataReaderSupplier;
    }

    /**
     * Creates reader of the metadata.
     *
     * @return reader, can not be {@code null}.
     * @param <T> type of the reader source
     */
    @Nonnull
    public <T> ClassesMetadataReader<T> createReader() {
        @SuppressWarnings("unchecked")
        final ClassesMetadataReader<T> reader = (ClassesMetadataReader<T>) this.metadataReaderSupplier.get();
        return reader;
    }

    /**
     * Converts string alias to enum value.
     * @param alias alias of the enum value, can not be {@code null}.
     * @return enum value, can not be {@code null}.
     */
    @Nonnull
    public static ClassesMetadataReaderType from(@Nonnull String alias) {
        if (JAR.alias.equalsIgnoreCase(alias)) {
            return JAR;
        } else if (DIRECTORY.alias.equalsIgnoreCase(alias)) {
            return DIRECTORY;
        } else {
            throw new IllegalArgumentException("Unsupported type of metadata reader: " + alias);
        }
    }
}
