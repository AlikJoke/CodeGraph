package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class CodeGraphCharacteristicFactoryTestBase<A extends Annotation, T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> {

    @Test
    public void test() {
        final var factory = createFactory();

        final A handleAnnotation = factory.getClass().getAnnotation(getFactoryHandleClass());
        assertNotNull(handleAnnotation, "Factory handle must be not null");

        final var handle = handleAnnotation.annotationType().getAnnotation(CodeGraphCharacteristicFactoryHandle.class);

        assertNotNull(handle.value(), "Definition id must be not null");
        assertFalse(handle.value().isEmpty(), "Definition id must be not empty");

        final var characteristic = factory.createCharacteristic(getParameters());
        assertNotNull(characteristic, "Created characteristic must be not null");
    }

    protected abstract CodeGraphCharacteristicFactory<T, V, K> createFactory();

    protected abstract Class<A> getFactoryHandleClass();

    protected abstract K getParameters();
}
