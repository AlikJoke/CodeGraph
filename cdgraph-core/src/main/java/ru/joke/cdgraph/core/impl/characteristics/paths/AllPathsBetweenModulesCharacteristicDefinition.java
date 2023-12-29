package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;

import javax.annotation.Nonnull;

public final class AllPathsBetweenModulesCharacteristicDefinition implements CodeGraphCharacteristicDefinition<AllPathsBetweenModulesCharacteristic, PathBetweenModulesCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "all.paths.between.modules";
    }

    @Nonnull
    @Override
    public AllPathsBetweenModulesCharacteristic createParameterizedCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        return new AllPathsBetweenModulesCharacteristic(parameters);
    }
}
