package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.graph.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_MODULE_3;

abstract class LocationsCharacteristicTestBase {

    protected void makeLocationsChecks(
            final String resourceName,
            final CodeGraph codeGraph,
            final boolean resourceShouldBeFound) {

        final var params = new ResourceLocationsCharacteristicParameters(resourceName);
        final var characteristic = createCharacteristic(params);

        final var result = characteristic.compute(codeGraph);
        final var locations = result.get();

        assertNotNull(locations, "Resource locations must be not null");
        assertEquals(resourceShouldBeFound ? 1 : 0, locations.size(), "Resource locations size must be equal");
        if (resourceShouldBeFound) {
            assertEquals(TEST_MODULE_3, locations.iterator().next().id(), "Resource location must be equal");
        }
    }

    @Nonnull
    protected abstract CodeGraphCharacteristic<Set<GraphNode>> createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters);
}
