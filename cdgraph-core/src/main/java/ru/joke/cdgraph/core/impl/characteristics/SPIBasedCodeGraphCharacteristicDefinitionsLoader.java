package ru.joke.cdgraph.core.impl.characteristics;

import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinitionsLoader;

import javax.annotation.Nonnull;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public final class SPIBasedCodeGraphCharacteristicDefinitionsLoader implements CodeGraphCharacteristicDefinitionsLoader {
    @Nonnull
    @Override
    public Set<CodeGraphCharacteristicDefinition<?, ?>> load() {
        @SuppressWarnings("rawtypes")
        final ServiceLoader<CodeGraphCharacteristicDefinition> serviceLoader = ServiceLoader.load(CodeGraphCharacteristicDefinition.class);
        return serviceLoader
                .stream()
                .map(ServiceLoader.Provider::get)
                .map(def -> (CodeGraphCharacteristicDefinition<?, ?>) def)
                .collect(Collectors.toSet());
    }
}
