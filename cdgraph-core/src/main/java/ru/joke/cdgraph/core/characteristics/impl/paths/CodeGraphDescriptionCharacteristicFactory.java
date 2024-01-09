package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryRegistryAware;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A factory of the characteristic that computes graph description in the form of all transitive
 * chains from the root node to all leaves.
 *
 * @author Alik
 *
 * @see CodeGraphDescriptionCharacteristic
 * @see CodeGraphDescriptionCharacteristicFactoryDescriptor
 */
@CodeGraphDescriptionCharacteristicFactoryDescriptor
public final class CodeGraphDescriptionCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<CodeGraphDescriptionCharacteristic, List<PathBetweenModules>, CodeGraphCharacteristicParameters>, CodeGraphCharacteristicFactoryRegistryAware {

    private CodeGraphCharacteristicService registry;

    @Nonnull
    @Override
    public CodeGraphDescriptionCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new CodeGraphDescriptionCharacteristic(this.characteristicId, this.registry);
    }

    @Override
    public void setRegistry(@Nonnull CodeGraphCharacteristicService registry) {
        this.registry = registry;
    }
}
