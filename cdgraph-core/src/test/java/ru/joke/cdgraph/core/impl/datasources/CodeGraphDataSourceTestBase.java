package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.impl.maven.MavenModuleCodeGraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class CodeGraphDataSourceTestBase {

    private static final String MODULE_INFO_CLASS = "module-info.class";
    private static final String GRADLE_BUILD_FILE = "/build.gradle";

    protected void makeChecks(
            final CodeGraphDataSource ds,
            final int expectedJavaModules,
            final int expectedJavaModulesMetadataClasses,
            final int expectedMavenModules,
            final int expectedMavenModulesMetadataClasses) {
        final var configs = ds.find(MODULE_INFO_CLASS::equals);
        assertEquals(expectedJavaModules, configs.size(), MODULE_INFO_CLASS + " should be found");

        final var warConfig = configs.getFirst();
        assertEquals(expectedJavaModulesMetadataClasses, warConfig.classesMetadata().size(), "Classes metadata size must be equal");

        final var mavenConfigs = ds.find(entry -> entry.endsWith(MavenModuleCodeGraph.POM_XML));
        assertEquals(expectedMavenModules, mavenConfigs.size(), MavenModuleCodeGraph.POM_XML + " should be found");

        final var mavenEarConfig = mavenConfigs.getFirst();
        assertEquals(expectedMavenModulesMetadataClasses, mavenEarConfig.classesMetadata().size(), "Classes metadata size must be equal");

        final var gradleConfigs = ds.find(GRADLE_BUILD_FILE::equals);
        assertTrue(gradleConfigs.isEmpty(), GRADLE_BUILD_FILE + " should not be found");
    }
}
