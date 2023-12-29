package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public record PathBetweenModulesCharacteristicParameters(
        @Nonnull String sourceModuleId,
        @Nonnull String targetModuleId) implements CodeGraphCharacteristicParameters {

    public PathBetweenModulesCharacteristicParameters {

        if (sourceModuleId.equals(targetModuleId)) {
            throw new CodeGraphCharacteristicConfigurationException("Source and target can not be the same");
        }
    }
}
