package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToIntCharacteristicParameterValueTransformerTest {

    @Test
    public void testValidStringValue() {
        final var transformer = new ToIntCharacteristicParameterValueTransformer();
        assertEquals(1, transformer.fromString("1"), "Transformed value must be equal");
    }

    @Test
    public void testInvalidStringValue() {
        final var transformer = new ToIntCharacteristicParameterValueTransformer();
        assertThrows(NumberFormatException.class, () -> transformer.fromString(""), "Exception must be thrown on empty value");
    }
}
