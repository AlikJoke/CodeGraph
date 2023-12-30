package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

@ConflictingDependenciesCharacteristicFactoryHandle
public final class ConflictingDependenciesCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<ConflictingDependenciesCharacteristic, Set<ConflictingDependencies>, CodeGraphCharacteristicParameters> {

    @Nonnull
    @Override
    public ConflictingDependenciesCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new ConflictingDependenciesCharacteristic(this.characteristicId);
    }
}
