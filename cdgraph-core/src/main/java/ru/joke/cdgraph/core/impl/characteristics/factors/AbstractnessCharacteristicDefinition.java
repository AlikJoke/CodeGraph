package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

public final class AbstractnessCharacteristicDefinition implements CodeGraphCharacteristicDefinition<AbstractnessCharacteristic, SingleModuleCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "module.abstractness";
    }

    @Nonnull
    @Override
    public AbstractnessCharacteristic createParameterizedCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new AbstractnessCharacteristic(parameters);
    }
}
