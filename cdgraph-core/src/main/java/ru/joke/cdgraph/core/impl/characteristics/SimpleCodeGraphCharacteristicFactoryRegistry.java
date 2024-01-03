package ru.joke.cdgraph.core.impl.characteristics;

import ru.joke.cdgraph.core.*;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple (and default) implementation of a factory registry based
 * on the {@link ConcurrentHashMap} in the memory.
 *
 * @author Alik
 * @see CodeGraphCharacteristicFactoryRegistry
 * @see CodeGraphCharacteristicFactoriesLoader
 */
public final class SimpleCodeGraphCharacteristicFactoryRegistry implements CodeGraphCharacteristicFactoryRegistry {

    private final Map<String, CodeGraphCharacteristicFactory<?, ?, ?>> factoriesMap = new ConcurrentHashMap<>();

    @Override
    public <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> void register(@Nonnull CodeGraphCharacteristicFactory<T, V, K> characteristicFactory) {
        final CodeGraphCharacteristicFactoryHandle annotation =
                Arrays.stream(characteristicFactory.getClass().getAnnotations())
                        .map(Annotation::annotationType)
                        .map(annotationType -> annotationType.getAnnotation(CodeGraphCharacteristicFactoryHandle.class))
                        .filter(Objects::nonNull)
                        .findAny()
                        .orElseThrow(() -> new CodeGraphConfigurationException("Required handle annotation not found"));

        if (characteristicFactory instanceof CodeGraphCharacteristicFactoryRegistryAware registryAware) {
            registryAware.setRegistry(this);
        }

        this.factoriesMap.putIfAbsent(annotation.value(), characteristicFactory);
    }

    @Override
    public void register(@Nonnull CodeGraphCharacteristicFactoriesLoader loader) {
        loader.load().forEach(this::register);
    }

    @Nonnull
    @Override
    public <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> find(@Nonnull String id) {
        @SuppressWarnings("unchecked")
        final var definition = (CodeGraphCharacteristicFactory<T, V, K>) this.factoriesMap.get(id);
        if (definition == null) {
            throw new CodeGraphCharacteristicFactoryNotFoundException(id);
        }

        return definition;
    }

    @Nonnull
    @Override
    public <A extends Annotation, T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> find(@Nonnull Class<A> characteristicFactoryHandle) {
        final var handle = characteristicFactoryHandle.getAnnotation(CodeGraphCharacteristicFactoryHandle.class);
        return find(handle.value());
    }
}
