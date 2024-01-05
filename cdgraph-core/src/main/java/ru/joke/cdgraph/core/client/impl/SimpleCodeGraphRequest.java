package ru.joke.cdgraph.core.client.impl;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.client.CodeGraphRequest;

import javax.annotation.Nonnull;
import java.util.List;

public record SimpleCodeGraphRequest(
        @Nonnull CodeGraph codeGraph,
        @Nonnull List<CodeGraphCharacteristic<?>> requiredCharacteristics) implements CodeGraphRequest {
}
