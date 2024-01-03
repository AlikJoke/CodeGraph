package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A factory of the characteristic that computes all transitive chains (paths)
 * from the specified module node.
 *
 * @author Alik
 *
 * @see TransitiveChainsCharacteristic
 * @see TransitiveChainsCharacteristicFactoryHandle
 */
@TransitiveChainsCharacteristicFactoryHandle
public final class TransitiveChainsCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<TransitiveChainsCharacteristic, List<PathBetweenModules>, SingleModuleCharacteristicParameters> {

    @Nonnull
    @Override
    public TransitiveChainsCharacteristic createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters) {
        return new TransitiveChainsCharacteristic(this.characteristicId, parameters);
    }
}
