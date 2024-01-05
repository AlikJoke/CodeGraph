package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class NullCharacteristicParameterDefaultValueFactoryTest {

    @Test
    public void test() {
        final var factory = new NullCharacteristicParameterDefaultValueFactory();
        assertNull(factory.create(), "Value must be null");
    }
}
