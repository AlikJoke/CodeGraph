package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

@ClassLocationsCharacteristicFactoryHandle
public final class ClassLocationsCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<ClassLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Nonnull
    @Override
    public ClassLocationsCharacteristic createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new ClassLocationsCharacteristic(this.characteristicId, parameters);
    }
}