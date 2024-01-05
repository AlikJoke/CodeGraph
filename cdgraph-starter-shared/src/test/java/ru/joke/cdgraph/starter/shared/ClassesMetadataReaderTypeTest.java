package ru.joke.cdgraph.starter.shared;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.meta.impl.FSDirectoryClassesMetadataReader;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import static org.junit.jupiter.api.Assertions.*;

public class ClassesMetadataReaderTypeTest {

    @Test
    public void testWhenEnumValueDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> ClassesMetadataReaderType.from("test"), "Exception must be thrown");
    }

    @Test
    public void testReaderCreation() {
        makeReaderCreationChecks(ClassesMetadataReaderType.JAR, JarClassesMetadataReader.class);
        makeReaderCreationChecks(ClassesMetadataReaderType.DIRECTORY, FSDirectoryClassesMetadataReader.class);
    }

    private void makeReaderCreationChecks(final ClassesMetadataReaderType readerType, final Class<? extends ClassesMetadataReader<?>> readerClass) {
        final var reader = readerType.createReader();
        assertNotNull(reader, "Reader must be not null");
        assertEquals(readerClass, reader.getClass(), "Reader type must be equal");
    }
}
