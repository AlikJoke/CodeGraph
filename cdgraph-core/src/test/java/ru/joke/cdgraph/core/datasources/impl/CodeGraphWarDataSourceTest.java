package ru.joke.cdgraph.core.datasources.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;

import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_WAR_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.getJarFile;

public class CodeGraphWarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        final CodeGraphDataSource ds = new CodeGraphWarDataSource(
                getJarFile(TEST_WAR_PATH).toPath(),
                new JarClassesMetadataReader(),
                new CodeGraphJarDataSourceFactory()
        );

        makeChecks(ds, 3, 7, 3, 7);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        final var factory = new CodeGraphWarDataSourceFactory(new CodeGraphJarDataSourceFactory());
        final var ds = factory.create(getJarFile(TEST_WAR_PATH).toPath(), new JarClassesMetadataReader());

        makeChecks(ds, 3, 7, 3, 7);
    }
}
