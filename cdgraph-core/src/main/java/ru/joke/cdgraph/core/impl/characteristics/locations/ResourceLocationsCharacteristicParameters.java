package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public record ResourceLocationsCharacteristicParameters(@Nonnull String resourceName) implements CodeGraphCharacteristicParameters {
}
