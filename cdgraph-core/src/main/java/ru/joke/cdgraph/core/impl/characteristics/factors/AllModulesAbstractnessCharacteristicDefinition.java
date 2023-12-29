package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import javax.annotation.Nonnull;

public final class AllModulesAbstractnessCharacteristicDefinition implements CodeGraphCharacteristicDefinition<AllModulesAbstractnessCharacteristic, CodeGraphCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "all.modules.abstractness";
    }

    @Nonnull
    @Override
    public AllModulesAbstractnessCharacteristic createParameterizedCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllModulesAbstractnessCharacteristic();
    }
}
