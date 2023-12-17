package ru.joke.cdgraph.core.std.ds;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphDataSource;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CodeGraphJarDataSourceTest {

    private static final String TEST_JAR_PATH = "/ds/test-modules3.jar";
    private static final String MODULE_INFO_CLASS = "module-info.class";
    private static final String MAVEN_POM_FILE = "/pom.xml";
    private static final String GRADLE_BUILD_FILE = "build.gradle";

    @Test
    public void test() throws URISyntaxException {
        final URL testJarUrl = getClass().getResource(TEST_JAR_PATH);
        final CodeGraphDataSource ds = new CodeGraphJarDataSource(Path.of(testJarUrl.toURI()));

        final var configs = ds.find(MODULE_INFO_CLASS::equals);
        assertEquals(1, configs.size(), MODULE_INFO_CLASS + " should be found");

        final var mavenConfigs = ds.find(entry -> entry.endsWith(MAVEN_POM_FILE));
        assertEquals(1, mavenConfigs.size(), MAVEN_POM_FILE + " should be found");

        final var gradleConfigs = ds.find(GRADLE_BUILD_FILE::equals);
        assertTrue(gradleConfigs.isEmpty(), GRADLE_BUILD_FILE + " should not be found");
    }
}
