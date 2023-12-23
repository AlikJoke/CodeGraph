package ru.joke.cdgraph.core.std.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public record ShortestPathBetweenNodesCharacteristicParameters(
        @Nonnull String sourceNodeId,
        @Nonnull String targetNodeId) implements CodeGraphCharacteristicParameters {

    public ShortestPathBetweenNodesCharacteristicParameters {

        if (sourceNodeId.equals(targetNodeId)) {
            throw new CodeGraphCharacteristicConfigurationException("Source and target can not be the same");
        }
    }
}
