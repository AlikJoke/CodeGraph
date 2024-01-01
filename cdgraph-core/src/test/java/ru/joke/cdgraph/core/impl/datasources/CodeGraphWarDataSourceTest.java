package ru.joke.cdgraph.core.impl.datasources;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_WAR_PATH;

public class CodeGraphWarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        final URL testWarUrl = getClass().getResource(TEST_WAR_PATH);
        final CodeGraphDataSource ds = new CodeGraphWarDataSource(
                Path.of(testWarUrl.toURI()),
                new JarClassesMetadataReader(),
                new CodeGraphJarDataSourceFactory()
        );

        makeChecks(ds, 3, 7, 3, 7);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        final URL testWarUrl = getClass().getResource(TEST_WAR_PATH);
        final var factory = new CodeGraphWarDataSourceFactory(new CodeGraphJarDataSourceFactory());
        final var ds = factory.create(Path.of(testWarUrl.toURI()), new JarClassesMetadataReader());

        makeChecks(ds, 3, 7, 3, 7);
    }
}
