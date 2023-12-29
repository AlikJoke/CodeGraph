package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;

import javax.annotation.Nonnull;

public final class ModuleClusteringCharacteristicDefinition implements
        CodeGraphCharacteristicDefinition<ModuleClusteringCharacteristic, ModuleClusteringCharacteristicParameters> {

    @Nonnull
    @Override
    public String id() {
        return "module.clustering";
    }

    @Nonnull
    @Override
    public ModuleClusteringCharacteristic createParameterizedCharacteristic(@Nonnull ModuleClusteringCharacteristicParameters parameters) {
        return new ModuleClusteringCharacteristic(parameters);
    }
}
