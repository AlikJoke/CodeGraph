package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;

public final class TransitiveChainsCharacteristicDefinition implements CodeGraphCharacteristicDefinition<TransitiveChainsCharacteristic, SingleModuleCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "transitive.chains";
    }

    @Nonnull
    @Override
    public TransitiveChainsCharacteristic createParameterizedCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new TransitiveChainsCharacteristic(parameters);
    }
}
