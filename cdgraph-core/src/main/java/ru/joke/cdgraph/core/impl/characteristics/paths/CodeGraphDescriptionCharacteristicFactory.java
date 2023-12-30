package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryRegistryAware;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.List;

@CodeGraphDescriptionCharacteristicFactoryHandle
public final class CodeGraphDescriptionCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<CodeGraphDescriptionCharacteristic, List<PathBetweenModules>, CodeGraphCharacteristicParameters>, CodeGraphCharacteristicFactoryRegistryAware {

    private CodeGraphCharacteristicFactoryRegistry registry;

    @Nonnull
    @Override
    public CodeGraphDescriptionCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new CodeGraphDescriptionCharacteristic(this.characteristicId, this.registry);
    }

    @Override
    public void setRegistry(@Nonnull CodeGraphCharacteristicFactoryRegistry registry) {
        this.registry = registry;
    }
}
