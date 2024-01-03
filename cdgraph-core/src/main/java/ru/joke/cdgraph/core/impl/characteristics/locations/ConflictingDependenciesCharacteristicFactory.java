package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A factory of the characteristic that computes conflicting dependencies
 * of the module in the code graph.
 *
 * @author Alik
 *
 * @see ConflictingDependenciesCharacteristic
 * @see ConflictingDependenciesCharacteristicFactoryHandle
 */
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
