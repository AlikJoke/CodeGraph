package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.*;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple (and default) implementation of a parameters' factory registry.
 *
 * @author Alik
 * @see CodeGraphCharacteristicParameter
 * @see CodeGraphCharacteristicParametersFactory
 * @see CodeGraphCharacteristicParametersFactory
 */
public final class SimpleCodeGraphCharacteristicParametersFactory implements CodeGraphCharacteristicParametersFactory {

    private final Map<Class<? extends CodeGraphCharacteristicParameterDefaultValueFactory<?>>, CodeGraphCharacteristicParameterDefaultValueFactory<?>> defaultValueFactoriesMap = new ConcurrentHashMap<>();
    private final Map<Class<? extends CodeGraphCharacteristicParameterValueTransformer<?>>, CodeGraphCharacteristicParameterValueTransformer<?>> valueTransformersMap = new ConcurrentHashMap<>();

    @Nonnull
    @Override
    public <K extends CodeGraphCharacteristicParameters> K createFor(
            @Nonnull CodeGraphCharacteristicFactory<?, ?, K> factory,
            @Nonnull Map<String, String> parametersMap) {

        final CodeGraphCharacteristicFactoryDescriptor descriptor = findFactoryDescriptor(factory);
        if (descriptor.characteristicParametersType() == CodeGraphCharacteristicParameters.EmptyParameters.class) {
            @SuppressWarnings("unchecked")
            final K result = (K) CodeGraphCharacteristicParameters.createEmpty();
            return result;
        }

        @SuppressWarnings("unchecked")
        final K result =
                (K) Arrays.stream(descriptor.characteristicParametersType().getConstructors())
                            .filter(this::isParametersFactory)
                            .findAny()
                            .map(constructor -> createParameters(constructor, parametersMap))
                            .orElseThrow(() -> new CodeGraphCharacteristicConfigurationException("No one factory constructor found for: " + factory.getClass()));

        return result;
    }

    private <K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactoryDescriptor findFactoryDescriptor(final CodeGraphCharacteristicFactory<?, ?, K> factory) {
        return Arrays.stream(factory.getClass().getAnnotations())
                        .map(Annotation::annotationType)
                        .map(annotationType -> annotationType.getAnnotation(CodeGraphCharacteristicFactoryDescriptor.class))
                        .filter(Objects::nonNull)
                        .findAny()
                        .orElseThrow(() -> new CodeGraphConfigurationException("Required descriptor annotation not found"));
    }

    private <K extends CodeGraphCharacteristicParameters> K createParameters(final Constructor<?> constructor, final Map<String, String> parameters) {

        final var constructorParameters = constructor.getParameters();
        final var constructorParameterValues = new Object[constructorParameters.length];

        for (int i = 0; i < constructorParameters.length; i++) {
            final var constructorParameter = constructorParameters[i];
            constructorParameterValues[i] = getParameterValue(constructorParameter, parameters);
        }

        try {
            @SuppressWarnings("unchecked")
            final K result = (K) constructor.newInstance(constructorParameterValues);
            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new CodeGraphCharacteristicConfigurationException(e);
        }
    }

    private Object getParameterValue(final Parameter parameter, final Map<String, String> parameters) {

        final var parameterDescriptor = parameter.getAnnotation(CodeGraphCharacteristicParameter.class);
        final var parameterValueFromMap = parameters.get(parameterDescriptor.id());
        if (parameterDescriptor == null) {
            throw new CodeGraphCharacteristicConfigurationException("Parameter annotation must present for: " + parameter.getName());
        }

        final var defaultValueFactory = parameterDescriptor.defaultValueFactory();
        final Object value = parameterValueFromMap == null
                ? getDefaultParameterValue(defaultValueFactory)
                : parameterValueFromMap;
        if (parameterDescriptor.required() && value == null) {
            throw new CodeGraphCharacteristicConfigurationException("Parameter value is required for: " + parameterDescriptor.id());
        }

        final var valueTransformerType = parameterDescriptor.valueTransformer();
        return value instanceof String v ? transformValue(valueTransformerType, v) : value;
    }

    private boolean isParametersFactory(final Constructor<?> factory) {
        return factory.getAnnotation(CodeGraphCharacteristicParametersCreator.class) != null;
    }

    private <T> T getClassInstance(final Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new CodeGraphCharacteristicConfigurationException(e);
        }
    }

    private Object transformValue(final Class<? extends CodeGraphCharacteristicParameterValueTransformer<?>> valueTransformerClass, final String value) {
        final var factory = this.valueTransformersMap.computeIfAbsent(valueTransformerClass, this::getClassInstance);
        return factory.fromString(value);
    }

    private Object getDefaultParameterValue(final Class<? extends CodeGraphCharacteristicParameterDefaultValueFactory<?>> defaultValueFactoryClass) {
        final var factory = this.defaultValueFactoriesMap.computeIfAbsent(defaultValueFactoryClass, this::getClassInstance);
        return factory.create();
    }
}
