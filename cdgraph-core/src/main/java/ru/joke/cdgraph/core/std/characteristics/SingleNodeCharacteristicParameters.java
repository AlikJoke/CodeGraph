package ru.joke.cdgraph.core.std.characteristics;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public record SingleNodeCharacteristicParameters(@Nonnull String nodeId) implements CodeGraphCharacteristicParameters {
}
