package ru.joke.cdgraph.core.impl.meta;

import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.CodeGraphDataSourceException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph.MODULE_INFO_CLASS;

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
