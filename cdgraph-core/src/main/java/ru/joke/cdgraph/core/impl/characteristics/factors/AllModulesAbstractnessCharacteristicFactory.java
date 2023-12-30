package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryRegistryAware;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.AbstractCodeGraphCharacteristicFactory;

import javax.annotation.Nonnull;
import java.util.Map;

@AllModulesAbstractnessCharacteristicFactoryHandle
public final class AllModulesAbstractnessCharacteristicFactory
        extends AbstractCodeGraphCharacteristicFactory
        implements CodeGraphCharacteristicFactory<AllModulesAbstractnessCharacteristic, Map<String, Factor>, CodeGraphCharacteristicParameters>, CodeGraphCharacteristicFactoryRegistryAware {

    private CodeGraphCharacteristicFactoryRegistry registry;

    @Nonnull
    @Override
    public AllModulesAbstractnessCharacteristic createCharacteristic(@Nonnull CodeGraphCharacteristicParameters parameters) {
        return new AllModulesAbstractnessCharacteristic(this.characteristicId, this.registry);
    }

    @Override
    public void setRegistry(@Nonnull CodeGraphCharacteristicFactoryRegistry registry) {
        this.registry = registry;
    }
}