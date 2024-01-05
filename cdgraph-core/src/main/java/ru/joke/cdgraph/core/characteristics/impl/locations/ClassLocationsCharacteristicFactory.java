package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes locations of the class with specified name.
 *
 * @author Alik
 *
 * @see ClassLocationsCharacteristic
 * @see ClassLocationsCharacteristicFactoryDescriptor
 */
@ClassLocationsCharacteristicFactoryDescriptor
public final class ClassLocationsCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<ClassLocationsCharacteristic, Set<GraphNode>, ResourceLocationsCharacteristicParameters> {

    @Nonnull
    @Override
    public ClassLocationsCharacteristic createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new ClassLocationsCharacteristic(this.characteristicId, parameters);
    }
}
