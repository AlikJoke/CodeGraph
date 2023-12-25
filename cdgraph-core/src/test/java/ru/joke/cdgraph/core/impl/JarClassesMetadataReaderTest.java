package ru.joke.cdgraph.core.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.ClassMetadata;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_JAR_PATH;

public class JarClassesMetadataReaderTest {

    private static final String TEST_PACKAGE_1 = "ru.joke.test";
    private static final String TEST_PACKAGE_2 = "ru.joke.test2";

    @Test
    public void test() throws IOException {
        final JarClassesMetadataReader reader = new JarClassesMetadataReader();

        final var jarUrl = getClass().getResource(TEST_JAR_PATH);
        try (final JarFile jar = new JarFile(new File(jarUrl.getFile()))) {
            final var allMetadata = reader.read(jar);

            assertEquals(7, allMetadata.size(), "Metadata count must be equal");
            final Map<String, ClassMetadata> metadataMap =
                    allMetadata
                            .stream()
                            .collect(Collectors.toMap(ClassMetadata::className, Function.identity()));

            makeMetadataChecks(metadataMap.get("TestClass"), ClassMetadata.ClassType.CONCRETE, TEST_PACKAGE_1);
            makeMetadataChecks(metadataMap.get("TestInterface"), ClassMetadata.ClassType.INTERFACE, TEST_PACKAGE_1);
            makeMetadataChecks(metadataMap.get("TestAbstractClass"), ClassMetadata.ClassType.ABSTRACT, TEST_PACKAGE_1);
            makeMetadataChecks(metadataMap.get("TestEnum"), ClassMetadata.ClassType.CONCRETE, TEST_PACKAGE_1);
            makeMetadataChecks(metadataMap.get("TestRecord"), ClassMetadata.ClassType.CONCRETE, TEST_PACKAGE_1);
            makeMetadataChecks(metadataMap.get("TestAnnotation"), ClassMetadata.ClassType.ANNOTATION, TEST_PACKAGE_1);
            makeMetadataChecks(metadataMap.get("TestFinalClass"), ClassMetadata.ClassType.CONCRETE, TEST_PACKAGE_2);
        }
    }

    private void makeMetadataChecks(
            final ClassMetadata metadata,
            final ClassMetadata.ClassType expectedType,
            final String expectedPackage) {

        assertNotNull(metadata, "Metadata must be not null");
        assertEquals(expectedType, metadata.classType(), "ClassType must be equal");
        assertEquals(expectedPackage, metadata.packageName(), "Package must be equal");
    }
}
