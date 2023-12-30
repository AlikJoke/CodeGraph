package ru.joke.cdgraph.core.impl.characteristics;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.CodeGraphConfigurationException;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractCodeGraphCharacteristicFactory {

    protected final String characteristicId;

    protected AbstractCodeGraphCharacteristicFactory() {
        final var handle =
                Arrays.stream(getClass().getAnnotations())
                        .map(Annotation::annotationType)
                        .map(annotationType -> annotationType.getAnnotation(CodeGraphCharacteristicFactoryHandle.class))
                        .filter(Objects::nonNull)
                        .findAny()
                        .orElseThrow(() -> new CodeGraphConfigurationException("Required handle annotation not found"));
        this.characteristicId = handle.value();
    }
}
