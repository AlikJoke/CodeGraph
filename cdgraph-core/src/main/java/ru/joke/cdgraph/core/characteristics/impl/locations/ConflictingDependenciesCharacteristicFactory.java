package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes conflicting dependencies
 * of the module in the code graph.
 *
 * @author Alik
 *
 * @see ConflictingDependenciesCharacteristic
 * @see ConflictingDependenciesCharacteristicFactoryDescriptor
 */
@ConflictingDependenciesCharacteristicFactoryDescriptor
public final class ConflictingDependenciesCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<ConflictingDependenciesCharacteristic, Set<ConflictingDependencies>, CodeGraphCharacteristicParameters> {

    @Nonnull
    @Override
    public ConflictingDependenciesCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new ConflictingDependenciesCharacteristic(this.characteristicId);
    }
}
