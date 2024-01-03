package ru.joke.cdgraph.core.impl.characteristics;

import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

/**
 * Parameters of the characteristics which computes the metrics of the single module.
 *
 * @param moduleId module identifier, can not be {@code null}.
 */
public record SingleModuleCharacteristicParameters(@Nonnull String moduleId) implements CodeGraphCharacteristicParameters {
}
