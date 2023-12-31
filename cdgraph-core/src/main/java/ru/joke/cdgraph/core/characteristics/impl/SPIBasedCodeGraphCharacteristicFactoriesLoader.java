package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoriesLoader;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;

import javax.annotation.Nonnull;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loader of the characteristic's factories implementation based on the Service Provider Interface (SPI).
 *
 * @author Alik
 * @see CodeGraphCharacteristicFactoriesLoader
 * @see CodeGraphCharacteristicService
 */
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
