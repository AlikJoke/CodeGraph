package ru.joke.cdgraph.core.impl.datasources;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_JAR_3_PATH;

public class CodeGraphJarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        final URL testJarUrl = getClass().getResource(TEST_JAR_3_PATH);
        final CodeGraphDataSource ds = new CodeGraphJarDataSource(Path.of(testJarUrl.toURI()), new JarClassesMetadataReader());
        makeChecks(ds, 1, 7, 1, 7);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        final URL testJarUrl = getClass().getResource(TEST_JAR_3_PATH);
        final var factory = new CodeGraphJarDataSourceFactory();
        final var ds = factory.create(Path.of(testJarUrl.toURI()));
        makeChecks(ds, 1, 7, 1, 7);
    }
}
