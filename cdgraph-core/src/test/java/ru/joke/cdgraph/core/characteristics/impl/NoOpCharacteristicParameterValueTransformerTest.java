package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NoOpCharacteristicParameterValueTransformerTest {

    @Test
    public void test() {
        final var transformer = new NoOpCharacteristicParameterValueTransformer();
        makeChecks(transformer, "");
        makeChecks(transformer, "1");
        makeChecks(transformer, "Test");
    }

    private void makeChecks(final NoOpCharacteristicParameterValueTransformer transformer, final String value) {
        assertEquals(value, transformer.fromString(value), "Transformed value must be equal");
    }
}
