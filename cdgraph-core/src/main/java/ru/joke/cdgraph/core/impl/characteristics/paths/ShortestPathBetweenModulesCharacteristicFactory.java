package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;

/**
 * A factory of the characteristic that computes shortest path between two modules of the graph.
 *
 * @author Alik
 *
 * @see ShortestPathBetweenModulesCharacteristic
 * @see ShortestPathBetweenModulesCharacteristicFactoryHandle
 */
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
