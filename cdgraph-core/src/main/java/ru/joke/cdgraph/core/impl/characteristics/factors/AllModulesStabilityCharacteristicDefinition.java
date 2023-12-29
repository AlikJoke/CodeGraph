package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public final class AllModulesStabilityCharacteristicDefinition implements CodeGraphCharacteristicDefinition<AllModulesStabilityCharacteristic, CodeGraphCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "all.modules.stability";
    }

    @Nonnull
    @Override
    public AllModulesStabilityCharacteristic createParameterizedCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllModulesStabilityCharacteristic();
    }
}
