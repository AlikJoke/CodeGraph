package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.CodeGraphDataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class CodeGraphDataSourceTestBase {

    private static final String MODULE_INFO_CLASS = "module-info.class";
    private static final String MAVEN_POM_FILE = "/pom.xml";
    private static final String GRADLE_BUILD_FILE = "/build.gradle";

    protected void makeChecks(
            final CodeGraphDataSource ds,
            final int expectedJavaModules,
            final int expectedJavaModulesMetadataClasses,
            final int expectedMavenModules,
            final int expectedMavenModulesMetadataClasses) {
        final var configs = ds.find(MODULE_INFO_CLASS::equals);
        final var warConfig = configs.get(0);
        assertEquals(expectedJavaModules, configs.size(), MODULE_INFO_CLASS + " should be found");
        assertEquals(expectedJavaModulesMetadataClasses, warConfig.classesMetadata().size(), "Classes metadata size must be equal");

        final var mavenConfigs = ds.find(entry -> entry.endsWith(MAVEN_POM_FILE));
        final var mavenEarConfig = mavenConfigs.get(0);
        assertEquals(expectedMavenModules, mavenConfigs.size(), MAVEN_POM_FILE + " should be found");
        assertEquals(expectedMavenModulesMetadataClasses, mavenEarConfig.classesMetadata().size(), "Classes metadata size must be equal");

        final var gradleConfigs = ds.find(GRADLE_BUILD_FILE::equals);
        assertTrue(gradleConfigs.isEmpty(), GRADLE_BUILD_FILE + " should not be found");
    }
}
