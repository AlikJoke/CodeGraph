package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.characteristics.impl.locations.AbstractSingleResourceLocationsCharacteristic.RESOURCE_NAME_TAG;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

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

            makeSingleModuleTagChecks(
                    codeGraph,
                    result.visualizedGraph(),
                    Set.of(TEST_MODULE_3),
                    TEST_MODULE_3,
                    RESOURCE_NAME_TAG,
                    resourceName
            );
        } else {
            makeGraphCloningChecks(codeGraph, result.visualizedGraph(), Collections.emptySet(), CodeGraph.CloneOptions.CLEAR_TAGS);
        }
    }

    @Nonnull
    protected abstract CodeGraphCharacteristic<Set<GraphNode>> createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters);
}
