package ru.joke.cdgraph.core.impl.characteristics.locations;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class AllClassesDuplicatesLocationsCharacteristicTest {

    @Test
    public void testSingleJarWithoutDuplicates() throws URISyntaxException {
        final var codeGraph = createCodeGraphByJar(TEST_JAR_3_PATH);
        final var characteristic = new AllClassesDuplicatesLocationsCharacteristic();
        final var result = characteristic.compute(codeGraph);

        final Set<ClassDuplicatesLocations> locations = result.get();
        assertNotNull(locations, "Locations must be not null");
        assertTrue(locations.isEmpty(), "Locations list must be empty");
    }

    @Test
    public void testTwoJarWithDuplicates() throws URISyntaxException {
        final var codeGraph = createCodeGraphByJar(TEST_JAR_3_PATH, TEST_JAR_2_PATH);
        final var characteristic = new AllClassesDuplicatesLocationsCharacteristic();
        final var result = characteristic.compute(codeGraph);

        final Set<ClassDuplicatesLocations> locations = result.get();
        assertNotNull(locations, "Locations must be not null");
        assertEquals(1, locations.size(), "Duplicates count must be equal");

        final var location = locations.iterator().next();
        assertEquals("ru.joke.test2.TestFinalClass", location.classQualifiedName(), "Duplicated class name must be equal");

        final var module2 = codeGraph.findNodeById(TEST_MODULE_2).orElseThrow();
        final var module3 = codeGraph.findNodeById(TEST_MODULE_3).orElseThrow();

        assertTrue(location.modules().contains(module2), "Duplicated class must be located in module #2");
        assertTrue(location.modules().contains(module3), "Duplicated class must be located in module #3");
    }
}
