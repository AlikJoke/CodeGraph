package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameter;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParametersCreator;

import javax.annotation.Nonnull;

/**
 * Parameters of the characteristics which defines the locations of a resource in the modules graph.
 *
 * @param resourceName name of the resource, can not be {@code null}.
 */
public record ResourceLocationsCharacteristicParameters(
        @CodeGraphCharacteristicParameter(id = "resource-name", required = true) @Nonnull String resourceName
) implements CodeGraphCharacteristicParameters {

    @CodeGraphCharacteristicParametersCreator
    public ResourceLocationsCharacteristicParameters {
        if (resourceName.isBlank()) {
            throw new CodeGraphCharacteristicConfigurationException("Resource name must be not empty");
        }
    }
}
