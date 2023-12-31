package ru.joke.cdgraph.core.impl.datasources;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_WAR_PATH;

public class CodeGraphWarDataSourceTest {

    private static final String MODULE_INFO_CLASS = "module-info.class";
    private static final String MAVEN_POM_FILE = "/pom.xml";
    private static final String GRADLE_BUILD_FILE = "/build.gradle";

    @Test
    public void testDirectCreation() throws URISyntaxException {
        final URL testWarUrl = getClass().getResource(TEST_WAR_PATH);
        final CodeGraphDataSource ds = new CodeGraphWarDataSource(
                Path.of(testWarUrl.toURI()),
                new JarClassesMetadataReader(),
                new CodeGraphJarDataSourceFactory()
        );

        makeChecks(ds);
    }

    @Test
    public void testCreationWithFactory() throws URISyntaxException {
        final URL testWarUrl = getClass().getResource(TEST_WAR_PATH);
        final var factory = new CodeGraphWarDataSourceFactory(new CodeGraphJarDataSourceFactory());
        final var ds = factory.create(Path.of(testWarUrl.toURI()), new JarClassesMetadataReader());

        makeChecks(ds);
    }

    private void makeChecks(final CodeGraphDataSource ds) {
        final var configs = ds.find(MODULE_INFO_CLASS::equals);
        final var warConfig = configs.get(0);
        assertEquals(3, configs.size(), MODULE_INFO_CLASS + " should be found");
        assertEquals(7, warConfig.classesMetadata().size(), "Classes metadata size must be equal");

        final var mavenConfigs = ds.find(entry -> entry.endsWith(MAVEN_POM_FILE));
        assertEquals(3, mavenConfigs.size(), MAVEN_POM_FILE + " should be found");
        assertEquals(7, warConfig.classesMetadata().size(), "Classes metadata size must be equal");

        final var gradleConfigs = ds.find(GRADLE_BUILD_FILE::equals);
        assertTrue(gradleConfigs.isEmpty(), GRADLE_BUILD_FILE + " should not be found");
    }
}
