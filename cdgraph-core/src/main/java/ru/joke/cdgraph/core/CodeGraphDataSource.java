package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The data source used to build the code modules graph. The data source abstraction can
 * mean either an archive of some type (WAR, EAR, JAR, FatJar) or a directory with a compiled
 * representation of the module. The data source is used to retrieve a configuration
 * that is specific to a modules graph of a certain type.<br>
 * The required implementation can be created by the corresponding factory implementation.
 *
 * @author Alik
 *
 * @see Configuration
 * @see ClassMetadata
 * @see CodeGraphDataSourceFactory
 */
public interface CodeGraphDataSource {

    /**
     * Returns the id of the data source.
     *
     * @return the if of the data source, can not be {@code null}.
     */
    @Nonnull
    String id();

    /**
     * Searches the data source for a configuration that matches the given predicate.
     *
     * @param descriptorFileFilter predicate, can not be {@code null}.
     * @return configurations that matches the given predicate, can not be {@code null}.
     * @see Configuration
     */
    @Nonnull
    List<Configuration> find(@Nonnull Predicate<String> descriptorFileFilter);

    /**
     * Configuration retrieved from the data source.
     * Contains a module descriptor and module class metadata.
     *
     * @param descriptor file descriptor of the module, can not be {@code null}.
     * @param classesMetadata set of the classes metadata, can not be {@code null}.
     */
    record Configuration(@Nonnull File descriptor, @Nonnull Set<ClassMetadata> classesMetadata) {
    }
}
