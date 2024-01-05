package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameter;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParametersCreator;

import javax.annotation.Nonnull;

/**
 * Parameters of the characteristics which computes the paths between two modules.
 *
 * @param sourceModuleId source module in the path, can not be {@code null}.
 * @param targetModuleId target module in the path, can not be {@code null}.
 */
public record PathBetweenModulesCharacteristicParameters(
        @CodeGraphCharacteristicParameter(id = "source-module-id", required = true) @Nonnull String sourceModuleId,
        @CodeGraphCharacteristicParameter(id = "target-module-id", required = true) @Nonnull String targetModuleId
) implements CodeGraphCharacteristicParameters {

    @CodeGraphCharacteristicParametersCreator
    public PathBetweenModulesCharacteristicParameters {

        if (sourceModuleId.isBlank() || sourceModuleId.equals(targetModuleId)) {
            throw new CodeGraphCharacteristicConfigurationException("Source and target can not be the same");
        }
    }
}
