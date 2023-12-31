package ru.joke.cdgraph.core.datasources.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;

import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_EAR_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.getJarFile;

public class CodeGraphEarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        final CodeGraphDataSource ds = new CodeGraphEarDataSource(
                getJarFile(TEST_EAR_PATH).toPath(),
                new JarClassesMetadataReader(),
                new CodeGraphWarDataSourceFactory()
        );

        makeChecks(ds, 3, 7, 4, 0);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        final var factory = new CodeGraphEarDataSourceFactory(new CodeGraphWarDataSourceFactory());
        final var ds = factory.create(getJarFile(TEST_EAR_PATH).toPath(), new JarClassesMetadataReader());

        makeChecks(ds, 3, 7, 4, 0);
    }
}
