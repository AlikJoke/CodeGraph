package ru.joke.cdgraph.core.impl.characteristics;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.characteristics.locations.ResourceLocationsCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_MODULE_3;

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
