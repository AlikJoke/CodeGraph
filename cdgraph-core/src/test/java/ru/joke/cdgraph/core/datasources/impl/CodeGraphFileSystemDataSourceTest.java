package ru.joke.cdgraph.core.datasources.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.meta.impl.FSDirectoryClassesMetadataReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_JAR_3_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.unpackTestJarToDirectory;

public class CodeGraphFileSystemDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException, IOException {
        final Path directoryWithClasses = unpackTestJarToDirectory(TEST_JAR_3_PATH);
        final CodeGraphDataSource ds = new CodeGraphFileSystemDataSource(directoryWithClasses, new FSDirectoryClassesMetadataReader());
        makeChecks(ds, 1, 7, 1, 7);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException, IOException {
        final Path directoryWithClasses = unpackTestJarToDirectory(TEST_JAR_3_PATH);
        final var factory = new CodeGraphFileSystemDataSourceFactory();
        final var ds = factory.create(directoryWithClasses);
        makeChecks(ds, 1, 7, 1, 7);
    }
}
