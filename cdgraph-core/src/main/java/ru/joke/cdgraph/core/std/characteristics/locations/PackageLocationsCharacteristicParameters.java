package ru.joke.cdgraph.core.std.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public record PackageLocationsCharacteristicParameters(@Nonnull String packageName) implements CodeGraphCharacteristicParameters {
}
