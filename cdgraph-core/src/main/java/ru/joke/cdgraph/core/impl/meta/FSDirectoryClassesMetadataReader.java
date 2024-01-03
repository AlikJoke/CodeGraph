package ru.joke.cdgraph.core.impl.meta;

import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.CodeGraphDataSourceException;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

import static ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph.MODULE_INFO_CLASS;

/**
 * Reader of the compiled classes metadata from the directory.
 *
 * @author Alik
 * @see ru.joke.cdgraph.core.ClassesMetadataReader
 */
public final class FSDirectoryClassesMetadataReader extends AbstractClassesMetadataReader<Path> {

    @Nonnull
    @Override
    public Set<ClassMetadata> read(@Nonnull Path classesDirectoryPath) {

        final Set<ClassMetadata> metadata = new HashSet<>();

        try {
            Files.walkFileTree(
                    classesDirectoryPath,
                    new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            final var fileName = file.getFileName().toString();
                            if (fileName.endsWith(CLASS_EXTENSION) && !fileName.endsWith(MODULE_INFO_CLASS)) {
                                metadata.add(readClassMetadata(file, classesDirectoryPath));
                            }

                            return FileVisitResult.CONTINUE;
                        }
                    });
        } catch (IOException ex) {
            throw new CodeGraphDataSourceException(ex);
        }

        return metadata;
    }

    private ClassMetadata readClassMetadata(final Path classFile, final Path classesDirPath) throws IOException {

        try (final InputStream is = new FileInputStream(classFile.toFile())) {
            final var classNameRelativePath = classesDirPath.relativize(classFile).toString();
            return read(is, classNameRelativePath.replace('\\', '/'));
        }
    }
}