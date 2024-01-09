package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicDescription;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameter;

import javax.annotation.Nonnull;
import java.util.Set;

record SimpleCodeGraphCharacteristicDescription(
        @Nonnull CodeGraphCharacteristicFactoryDescriptor factoryDescriptor,
        @Nonnull Set<CodeGraphCharacteristicParameter> parameters
) implements CodeGraphCharacteristicDescription {
}
