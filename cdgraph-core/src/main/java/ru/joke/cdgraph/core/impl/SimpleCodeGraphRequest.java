package ru.joke.cdgraph.core.impl;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphRequest;

import javax.annotation.Nonnull;
import java.util.List;

public record SimpleCodeGraphRequest(
        @Nonnull CodeGraph codeGraph,
        @Nonnull List<CodeGraphCharacteristic<?>> requiredCharacteristics) implements CodeGraphRequest {
}
