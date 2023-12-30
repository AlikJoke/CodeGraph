package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;

@ShortestPathBetweenModulesCharacteristicFactoryHandle
public final class ShortestPathBetweenModulesCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<ShortestPathBetweenModulesCharacteristic, PathBetweenModules, PathBetweenModulesCharacteristicParameters> {

    @Nonnull
    @Override
    public ShortestPathBetweenModulesCharacteristic createCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        return new ShortestPathBetweenModulesCharacteristic(this.characteristicId, parameters);
    }
}
