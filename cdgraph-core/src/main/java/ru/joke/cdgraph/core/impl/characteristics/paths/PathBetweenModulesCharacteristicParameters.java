package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * Parameters of the characteristics which computes the paths between two modules.
 *
 * @param sourceModuleId source module in the path, can not be {@code null}.
 * @param targetModuleId target module in the path, can not be {@code null}.
 */
public record PathBetweenModulesCharacteristicParameters(
        @Nonnull String sourceModuleId,
        @Nonnull String targetModuleId) implements CodeGraphCharacteristicParameters {

    public PathBetweenModulesCharacteristicParameters {

        if (sourceModuleId.equals(targetModuleId)) {
            throw new CodeGraphCharacteristicConfigurationException("Source and target can not be the same");
        }
    }
}
