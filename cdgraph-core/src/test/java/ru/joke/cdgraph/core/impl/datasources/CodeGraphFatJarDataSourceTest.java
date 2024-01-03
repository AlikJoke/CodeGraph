package ru.joke.cdgraph.core.impl.datasources;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.impl.meta.JarClassesMetadataReader;

import java.net.URISyntaxException;

import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_WAR_PATH;
import static ru.joke.cdgraph.core.impl.util.TestUtil.getJarFile;

public class CodeGraphFatJarDataSourceTest extends CodeGraphDataSourceTestBase {

    @Test
    public void testDirectCreation() throws URISyntaxException {
        // structure of war similar to fat/uber jar
        final CodeGraphDataSource ds = new CodeGraphFatJarDataSource(
                getJarFile(TEST_WAR_PATH).toPath(),
                new JarClassesMetadataReader(),
                new CodeGraphJarDataSourceFactory()
        );

        makeChecks(ds, 3, 7, 3, 7);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        // structure of war similar to fat/uber jar
        final var factory = new CodeGraphFatJarDataSourceFactory(new CodeGraphJarDataSourceFactory());
        final var ds = factory.create(getJarFile(TEST_WAR_PATH).toPath(), new JarClassesMetadataReader());

        makeChecks(ds, 3, 7, 3, 7);
    }
}
