package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;
import ru.joke.cdgraph.core.impl.characteristics.locations.PackageLocationsCharacteristic;
import ru.joke.cdgraph.core.impl.characteristics.locations.PackageLocationsCharacteristicParameters;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphJarDataSource;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_JAR_PATH;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_MODULE_3;

public class PackageLocationsCharacteristicTest {

    @Test
    public void test() throws URISyntaxException {
        final var jarUrl = getClass().getResource(TEST_JAR_PATH);
        final var jarPath = Path.of(jarUrl.toURI());
        final var codeGraph = new JavaModuleCodeGraph(new CodeGraphJarDataSource(jarPath, new JarClassesMetadataReader()));

        makeLocationsChecks("ru.joke.test", codeGraph, true);
        makeLocationsChecks("ru.joke", codeGraph, true);
        makeLocationsChecks("ru.joke.test2", codeGraph, true);
        makeLocationsChecks("ru.joke.test3", codeGraph, false);
    }

    private void makeLocationsChecks(final String packageName, final CodeGraph codeGraph, final boolean moduleShouldBeFound) {

        final var params = new PackageLocationsCharacteristicParameters(packageName);
        final var characteristic = new PackageLocationsCharacteristic(params);

        final var result = characteristic.compute(codeGraph);
        final var locations = result.get();

        assertNotNull(locations, "Package locations must be not null");
        assertEquals(moduleShouldBeFound ? 1 : 0, locations.size(), "Package locations size must be equal");
        if (moduleShouldBeFound) {
            assertEquals(TEST_MODULE_3, locations.iterator().next(), "Package location must be equal");
        }
    }
}
