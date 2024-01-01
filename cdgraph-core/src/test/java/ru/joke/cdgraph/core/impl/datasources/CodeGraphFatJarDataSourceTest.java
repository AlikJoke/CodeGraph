package ru.joke.cdgraph.core.impl.datasources;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_WAR_PATH;

public class CodeGraphFatJarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        // structure of war similar to fat/uber jar
        final URL testFatJarUrl = getClass().getResource(TEST_WAR_PATH);
        final CodeGraphDataSource ds = new CodeGraphFatJarDataSource(
                Path.of(testFatJarUrl.toURI()),
                new JarClassesMetadataReader(),
                new CodeGraphJarDataSourceFactory()
        );

        makeChecks(ds, 3, 7, 3, 7);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        // structure of war similar to fat/uber jar
        final URL testFatJarUrl = getClass().getResource(TEST_WAR_PATH);
        final var factory = new CodeGraphFatJarDataSourceFactory(new CodeGraphJarDataSourceFactory());
        final var ds = factory.create(Path.of(testFatJarUrl.toURI()), new JarClassesMetadataReader());

        makeChecks(ds, 3, 7, 3, 7);
    }
}
