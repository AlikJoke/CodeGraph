package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.*;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple (and default) implementation of a factory registry based
 * on the {@link ConcurrentHashMap} in the memory.
 *
 * @author Alik
 * @see CodeGraphCharacteristicService
 * @see CodeGraphCharacteristicFactoriesLoader
 */
public final class SimpleCodeGraphCharacteristicService implements CodeGraphCharacteristicService {

    private final Map<String, CodeGraphCharacteristicDescription> descriptionsMap = new ConcurrentHashMap<>();
    private final Map<String, CodeGraphCharacteristicFactory<?, ?, ?>> factoriesMap = new ConcurrentHashMap<>();

    @Override
    public <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> void registerFactory(@Nonnull CodeGraphCharacteristicFactory<T, V, K> characteristicFactory) {
        final CodeGraphCharacteristicFactoryDescriptor annotation =
                Arrays.stream(characteristicFactory.getClass().getAnnotations())
                        .map(Annotation::annotationType)
                        .map(annotationType -> annotationType.getAnnotation(CodeGraphCharacteristicFactoryDescriptor.class))
                        .filter(Objects::nonNull)
                        .findAny()
                        .orElseThrow(() -> new CodeGraphConfigurationException("Required descriptor annotation not found"));

        if (characteristicFactory instanceof CodeGraphCharacteristicFactoryRegistryAware registryAware) {
            registryAware.setRegistry(this);
        }

        Arrays.stream(annotation.characteristicParametersType().getConstructors())
                .filter(constructor -> constructor.getAnnotation(CodeGraphCharacteristicParametersCreator.class) != null)
                .map(constructor -> createDescription(annotation, constructor))
                .findAny()
                .ifPresentOrElse(
                        description -> this.descriptionsMap.putIfAbsent(annotation.characteristicId(), description),
                        () -> this.descriptionsMap.putIfAbsent(annotation.characteristicId(), createDescription(annotation))
                );

        this.factoriesMap.putIfAbsent(annotation.characteristicId(), characteristicFactory);
    }

    @Override
    public void registerFactories(@Nonnull CodeGraphCharacteristicFactoriesLoader loader) {
        loader.load().forEach(this::registerFactory);
    }

    @Nonnull
    @Override
    public <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> findFactory(@Nonnull String id) {
        @SuppressWarnings("unchecked")
        final var definition = (CodeGraphCharacteristicFactory<T, V, K>) this.factoriesMap.get(id);
        if (definition == null) {
            throw new CodeGraphCharacteristicFactoryNotFoundException(id);
        }

        return definition;
    }

    @Nonnull
    @Override
    public <A extends Annotation, T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> findFactory(@Nonnull Class<A> characteristicFactoryDescriptor) {
        final var descriptor = characteristicFactoryDescriptor.getAnnotation(CodeGraphCharacteristicFactoryDescriptor.class);
        return findFactory(descriptor.characteristicId());
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicDescription findDescription(@Nonnull String characteristicId) {
        final var description = this.descriptionsMap.get(characteristicId);
        if (description == null) {
            throw new CodeGraphCharacteristicDescriptionNotFoundException(characteristicId);
        }

        return description;
    }

    @Nonnull
    @Override
    public Set<CodeGraphCharacteristicDescription> findDescriptions() {
        return new HashSet<>(this.descriptionsMap.values());
    }

    private CodeGraphCharacteristicDescription createDescription(final CodeGraphCharacteristicFactoryDescriptor factoryDescriptor) {
        return new SimpleCodeGraphCharacteristicDescription(factoryDescriptor, Collections.emptySet());
    }

    private CodeGraphCharacteristicDescription createDescription(
            final CodeGraphCharacteristicFactoryDescriptor factoryDescriptor,
            final Constructor<?> constructor) {
        final var parameters = findParameters(constructor);
        return new SimpleCodeGraphCharacteristicDescription(factoryDescriptor, parameters);
    }

    private Set<CodeGraphCharacteristicParameter> findParameters(final Constructor<?> constructor) {
        final var constructorParameters = constructor.getParameters();

        final Set<CodeGraphCharacteristicParameter> parameters = new HashSet<>();
        for (final Parameter constructorParameter : constructorParameters) {
            final var parameter = constructorParameter.getAnnotation(CodeGraphCharacteristicParameter.class);

            parameters.add(parameter);
        }

        return parameters;
    }
}
