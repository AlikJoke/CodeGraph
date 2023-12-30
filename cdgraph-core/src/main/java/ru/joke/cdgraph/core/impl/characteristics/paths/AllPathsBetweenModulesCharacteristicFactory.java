package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.List;

@AllPathsBetweenModulesCharacteristicFactoryHandle
public final class AllPathsBetweenModulesCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AllPathsBetweenModulesCharacteristic, List<PathBetweenModules>, PathBetweenModulesCharacteristicParameters> {

    @Nonnull
    @Override
    public AllPathsBetweenModulesCharacteristic createCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        return new AllPathsBetweenModulesCharacteristic(this.characteristicId, parameters);
    }
}
