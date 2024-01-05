package ru.joke.cdgraph.core.meta.impl;

import ru.joke.cdgraph.core.meta.ClassMetadata;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceException;
import ru.joke.cdgraph.core.meta.ClassesMetadataReader;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph.MODULE_INFO_CLASS;

/**
 * Reader of the classes metadata from JAR.
 *
 * @author Alik
 * @see ClassesMetadataReader
 */
public final class JarClassesMetadataReader extends AbstractClassesMetadataReader<JarFile> {

    @Nonnull
    @Override
    public Set<ClassMetadata> read(@Nonnull JarFile jar) {

        return jar
                .stream()
                .filter(entry -> entry.getName().endsWith(CLASS_EXTENSION) && !entry.getName().endsWith(MODULE_INFO_CLASS))
                .map(entry -> read(jar, entry))
                .collect(Collectors.toSet());
    }

    private ClassMetadata read(@Nonnull JarFile jar, @Nonnull JarEntry classEntry) {

        try (final InputStream is = jar.getInputStream(classEntry)) {
            return read(is, classEntry.getName());
        } catch (IOException e) {
            throw new CodeGraphDataSourceException(e);
        }
    }
}
