package ru.joke.cdgraph.core.impl.datasources;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_EAR_PATH;

public class CodeGraphEarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        final URL testEarUrl = getClass().getResource(TEST_EAR_PATH);
        final CodeGraphDataSource ds = new CodeGraphEarDataSource(
                Path.of(testEarUrl.toURI()),
                new JarClassesMetadataReader(),
                new CodeGraphWarDataSourceFactory()
        );

        makeChecks(ds, 3, 7, 4, 0);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        final URL testEarUrl = getClass().getResource(TEST_EAR_PATH);
        final var factory = new CodeGraphEarDataSourceFactory(new CodeGraphWarDataSourceFactory());
        final var ds = factory.create(Path.of(testEarUrl.toURI()), new JarClassesMetadataReader());

        makeChecks(ds, 3, 7, 4, 0);
    }
}
