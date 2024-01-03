package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Data source implementation for the compiled sources from the file system.<br>
 * A factory can be used to create: {@link CodeGraphFileSystemDataSourceFactory}.
 *
 * @author Alik
 * @see ru.joke.cdgraph.core.CodeGraphDataSource
 */
public final class CodeGraphFileSystemDataSource implements CodeGraphDataSource {

    private static final String CLASSES_DIR = "classes";

    private final Path rootProjectDirectory;
    private final ClassesMetadataReader<Path> metadataReader;

    public CodeGraphFileSystemDataSource(
            @Nonnull Path rootProjectDirectory,
            @Nonnull ClassesMetadataReader<Path> metadataReader) {
        this.rootProjectDirectory = rootProjectDirectory;
        this.metadataReader = metadataReader;
    }

    @Nonnull
    @Override
    public String id() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public List<Configuration> find(@Nonnull Predicate<String> descriptorFileFilter) {

        final List<Configuration> configs = new ArrayList<>();

        try {
            Files.walkFileTree(
                    this.rootProjectDirectory,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (descriptorFileFilter.test(file.getFileName().toString())) {
                                configs.add(buildConfiguration(file));
                            }

                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
        } catch (IOException ex) {
            throw new CodeGraphDataSourceException(ex);
        }

        return configs;
    }

    private Configuration buildConfiguration(final Path configFile) throws IOException {

        final var parentDir = configFile.getParent();
        final var classesDirs = findClassesDirs(parentDir);

        try (classesDirs) {
            final var classesMetadata = classesDirs
                                            .findAny()
                                            .map(metadataReader::read)
                                            .orElse(Collections.emptySet());
            return new Configuration(configFile.toFile(), classesMetadata);
        }
    }

    private Stream<Path> findClassesDirs(final Path parentDir) throws IOException {
        if (isClassesDir(parentDir)) {
            return Stream.of(parentDir);
        }

        return Files.find(parentDir, 2, (path, attr) -> isClassesDir(path));
    }

    private boolean isClassesDir(final Path path) {
        return path.getFileName().toString().equals(CLASSES_DIR) && Files.isDirectory(path);
    }
}
