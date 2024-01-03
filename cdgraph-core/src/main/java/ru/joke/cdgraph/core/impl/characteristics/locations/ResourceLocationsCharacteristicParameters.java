package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * Parameters of the characteristics which defines the locations of a resource in the modules graph.
 *
 * @param resourceName name of the resource, can not be {@code null}.
 */
public record ResourceLocationsCharacteristicParameters(@Nonnull String resourceName) implements CodeGraphCharacteristicParameters {
}
