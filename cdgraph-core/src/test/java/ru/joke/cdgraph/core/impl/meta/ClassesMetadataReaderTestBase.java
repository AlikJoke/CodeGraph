package ru.joke.cdgraph.core.impl.meta;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.ClassMetadata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class ClassesMetadataReaderTestBase {

    private static final String TEST_PACKAGE_1 = "ru.joke.test";
    private static final String TEST_PACKAGE_2 = "ru.joke.test2";

    @Test
    public void test() throws IOException, URISyntaxException {

        final var allMetadata = readMetadata();

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

    protected abstract Set<ClassMetadata> readMetadata() throws IOException, URISyntaxException;

    private void makeMetadataChecks(
            final ClassMetadata metadata,
            final ClassMetadata.ClassType expectedType,
            final String expectedPackage) {

        assertNotNull(metadata, "Metadata must be not null");
        assertEquals(expectedType, metadata.classType(), "ClassType must be equal");
        assertEquals(expectedPackage, metadata.packageName(), "Package must be equal");
    }
}
