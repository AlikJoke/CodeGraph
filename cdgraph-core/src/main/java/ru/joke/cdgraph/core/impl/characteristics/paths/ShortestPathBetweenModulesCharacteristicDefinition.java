package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;

import javax.annotation.Nonnull;

public final class ShortestPathBetweenModulesCharacteristicDefinition implements CodeGraphCharacteristicDefinition<ShortestPathBetweenModulesCharacteristic, PathBetweenModulesCharacteristicParameters> {
    @Nonnull
    @Override
    public String id() {
        return "shortest.path.between.modules";
    }

    @Nonnull
    @Override
    public ShortestPathBetweenModulesCharacteristic createParameterizedCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        return new ShortestPathBetweenModulesCharacteristic(parameters);
    }
}
