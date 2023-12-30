package ru.joke.cdgraph.core.impl.characteristics;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoriesLoader;

import javax.annotation.Nonnull;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public final class SPIBasedCodeGraphCharacteristicFactoriesLoader implements CodeGraphCharacteristicFactoriesLoader {
    @Nonnull
    @Override
    public Set<CodeGraphCharacteristicFactory<?, ?, ?>> load() {
        @SuppressWarnings("rawtypes")
        final ServiceLoader<CodeGraphCharacteristicFactory> serviceLoader = ServiceLoader.load(CodeGraphCharacteristicFactory.class);
        return serviceLoader
                .stream()
                .map(ServiceLoader.Provider::get)
                .map(def -> (CodeGraphCharacteristicFactory<?, ?, ?>) def)
                .collect(Collectors.toSet());
    }
}
