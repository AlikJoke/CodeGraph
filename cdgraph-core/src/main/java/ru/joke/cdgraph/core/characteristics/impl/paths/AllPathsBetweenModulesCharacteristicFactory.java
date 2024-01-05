package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A factory of the characteristic that computes all paths between two modules of the graph.
 *
 * @author Alik
 *
 * @see AllPathsBetweenModulesCharacteristic
 * @see AllPathsBetweenModulesCharacteristicFactoryDescriptor
 */
@AllPathsBetweenModulesCharacteristicFactoryDescriptor
public final class AllPathsBetweenModulesCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AllPathsBetweenModulesCharacteristic, List<PathBetweenModules>, PathBetweenModulesCharacteristicParameters> {

    @Nonnull
    @Override
    public AllPathsBetweenModulesCharacteristic createCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        return new AllPathsBetweenModulesCharacteristic(this.characteristicId, parameters);
    }
}
