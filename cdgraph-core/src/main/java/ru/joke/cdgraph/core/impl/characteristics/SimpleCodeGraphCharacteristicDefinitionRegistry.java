package ru.joke.cdgraph.core.impl.characteristics;

import ru.joke.cdgraph.core.*;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SimpleCodeGraphCharacteristicDefinitionRegistry implements CodeGraphCharacteristicDefinitionRegistry {

    private final Map<String, CodeGraphCharacteristicDefinition<?, ?>> definitionsMap = new ConcurrentHashMap<>();

    @Override
    public <T extends CodeGraphCharacteristic<?>, K extends CodeGraphCharacteristicParameters> void register(@Nonnull CodeGraphCharacteristicDefinition<T, K> characteristicDefinition) {
        this.definitionsMap.putIfAbsent(characteristicDefinition.id(), characteristicDefinition);
    }

    @Override
    public void register(@Nonnull CodeGraphCharacteristicDefinitionsLoader loader) {
        loader.load().forEach(this::register);
    }

    @Nonnull
    @Override
    public <T extends CodeGraphCharacteristic<?>, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicDefinition<T, K> find(@Nonnull String id) {
        @SuppressWarnings("unchecked")
        final var definition = (CodeGraphCharacteristicDefinition<T, K>) this.definitionsMap.get(id);
        if (definition == null) {
            throw new CodeGraphCharacteristicDefinitionNotFoundException(id);
        }

        return definition;
    }
}
