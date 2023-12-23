package ru.joke.cdgraph.core.std.characteristics.counters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public record DependenciesCountCharacteristicParameters(@Nonnull String nodeId) implements CodeGraphCharacteristicParameters {
}
