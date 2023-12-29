package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

public final class StabilityCharacteristicDefinition implements CodeGraphCharacteristicDefinition<StabilityCharacteristic, SingleModuleCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "module.stability";
    }

    @Nonnull
    @Override
    public StabilityCharacteristic createParameterizedCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new StabilityCharacteristic(parameters);
    }
}
