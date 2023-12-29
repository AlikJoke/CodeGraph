package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class CodeGraphCharacteristicDefinitionTestBase<T extends CodeGraphCharacteristic<?>, K extends CodeGraphCharacteristicParameters> {

    @Test
    public void test() {
        final var definition = createDefinition();

        assertNotNull(definition.id(), "Definition id must be not null");
        assertFalse(definition.id().isEmpty(), "Definition id must be not empty");

        final var characteristic = definition.createParameterizedCharacteristic(getParameters());
        assertNotNull(characteristic, "Created characteristic must be not null");
    }

    protected abstract CodeGraphCharacteristicDefinition<T, K> createDefinition();

    protected abstract K getParameters();
}
