package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractCodeGraphCharacteristicFactory {

    protected final String characteristicId;

    protected AbstractCodeGraphCharacteristicFactory() {
        final var descriptor =
                Arrays.stream(getClass().getAnnotations())
                        .map(Annotation::annotationType)
                        .map(annotationType -> annotationType.getAnnotation(CodeGraphCharacteristicFactoryDescriptor.class))
                        .filter(Objects::nonNull)
                        .findAny()
                        .orElseThrow(() -> new CodeGraphConfigurationException("Required descriptor annotation not found"));
        this.characteristicId = descriptor.characteristicId();
    }
}
