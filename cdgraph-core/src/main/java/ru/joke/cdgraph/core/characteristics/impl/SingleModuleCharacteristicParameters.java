package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameter;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParametersCreator;

import javax.annotation.Nonnull;

/**
 * Parameters of the characteristics which computes the metrics of the single module.
 *
 * @param moduleId module identifier, can not be {@code null}.
 */
public record SingleModuleCharacteristicParameters(
        @CodeGraphCharacteristicParameter(
                id = "module-id",
                description = "Identifier of the module for which the characteristic is computed",
                required = true
        ) @Nonnull String moduleId
) implements CodeGraphCharacteristicParameters {

    @CodeGraphCharacteristicParametersCreator
    public SingleModuleCharacteristicParameters {
        if (moduleId.isBlank()) {
            throw new CodeGraphCharacteristicConfigurationException("Module id must be not empty");
        }
    }
}
