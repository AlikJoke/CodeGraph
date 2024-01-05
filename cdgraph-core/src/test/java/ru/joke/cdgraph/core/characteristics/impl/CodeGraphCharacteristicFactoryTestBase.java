package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;

public abstract class CodeGraphCharacteristicFactoryTestBase<A extends Annotation, T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> {

    @Test
    public void test() {
        final var factory = createFactory();

        final A descriptorAnnotation = factory.getClass().getAnnotation(getFactoryDescriptorClass());
        assertNotNull(descriptorAnnotation, "Factory descriptor must be not null");

        final var descriptor = descriptorAnnotation.annotationType().getAnnotation(CodeGraphCharacteristicFactoryDescriptor.class);

        assertNotNull(descriptor.characteristicId(), "Definition id must be not null");
        assertFalse(descriptor.characteristicId().isEmpty(), "Definition id must be not empty");

        assertEquals(getParameters().getClass(), descriptor.characteristicParametersType(), "Parameters type must be equal");

        final var characteristic = factory.createCharacteristic(getParameters());
        assertNotNull(characteristic, "Created characteristic must be not null");
    }

    protected abstract CodeGraphCharacteristicFactory<T, V, K> createFactory();

    protected abstract Class<A> getFactoryDescriptorClass();

    protected abstract K getParameters();
}
