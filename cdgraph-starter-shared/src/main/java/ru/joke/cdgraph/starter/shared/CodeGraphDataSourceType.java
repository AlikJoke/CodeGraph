package ru.joke.cdgraph.starter.shared;

import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceFactory;
import ru.joke.cdgraph.core.datasources.impl.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Types of data sources.
 *
 * @author Alik
 * @see ru.joke.cdgraph.core.datasources.CodeGraphDataSource
 * @see CodeGraphDataSourceFactory
 */
public enum CodeGraphDataSourceType {

    /**
     * EAR source
     */
    EAR("ear"),

    /**
     * WAR source
     */
    WAR("war"),

    /**
     * Fat JAR source
     */
    FAT_JAR("fat-jar"),

    /**
     * Plain JAR source (skinny)
     */
    JAR("jar"),

    /**
     * File system directory source
     */
    DIRECTORY("directory");

    private final String alias;

    CodeGraphDataSourceType(@Nonnull String alias) {
        this.alias = alias;
    }

    /**
     * Returns the alias of the data source.
     * @return alias of the data source, can not be {@code null}.
     */
    @Nonnull
    public String getAlias() {
        return this.alias;
    }

    /**
     * Converts alias string to enum value.
     *
     * @param alias alias of the enum value, can not be {@code null}.
     * @return enum value, can not be {@code null}.
     */
    @Nonnull
    public static CodeGraphDataSourceType from(@Nonnull String alias) {
        return Arrays.stream(CodeGraphDataSourceType.values())
                        .filter(type -> type.alias.equalsIgnoreCase(alias))
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("Unsupported type of data source: " + alias));
    }

    /**
     * Creates {@link CodeGraphDataSourceFactory} by the data source type.
     *
     * @param dataSourceType type of the root source, can not be {@code null}.
     * @param nestedDataSourceType type of the nested source, if root source contains it, can not be {@code null}.
     * @return created factory, can not be {@code null}.
     * @param <T> type of the source ({@link java.util.jar.JarFile} or {@link java.nio.file.Path})
     */
    @Nonnull
    public static <T> CodeGraphDataSourceFactory<T> createFactory(
            @Nonnull CodeGraphDataSourceType dataSourceType,
            @Nullable CodeGraphDataSourceType nestedDataSourceType) {
        final CodeGraphDataSourceFactory<?> dataSourceFactory = switch (dataSourceType) {
            case EAR -> nestedDataSourceType == null ? new CodeGraphEarDataSourceFactory() : new CodeGraphEarDataSourceFactory(createFactory(nestedDataSourceType, null));
            case WAR -> nestedDataSourceType == null ? new CodeGraphWarDataSourceFactory() : new CodeGraphWarDataSourceFactory(createFactory(nestedDataSourceType, null));
            case FAT_JAR -> new CodeGraphFatJarDataSourceFactory();
            case JAR -> new CodeGraphJarDataSourceFactory();
            case DIRECTORY -> new CodeGraphFileSystemDataSourceFactory();
        };

        @SuppressWarnings("unchecked")
        final CodeGraphDataSourceFactory<T> result = (CodeGraphDataSourceFactory<T>) dataSourceFactory;
        return result;
    }
}
