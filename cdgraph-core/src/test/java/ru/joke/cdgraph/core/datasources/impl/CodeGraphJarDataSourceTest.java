package ru.joke.cdgraph.core.datasources.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;

import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_JAR_3_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.getJarFile;

public class CodeGraphJarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        final CodeGraphDataSource ds = new CodeGraphJarDataSource(getJarFile(TEST_JAR_3_PATH).toPath(), new JarClassesMetadataReader());
        makeChecks(ds, 1, 7, 1, 7);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        final var factory = new CodeGraphJarDataSourceFactory();
        final var ds = factory.create(getJarFile(TEST_JAR_3_PATH).toPath());
        makeChecks(ds, 1, 7, 1, 7);
    }
}
