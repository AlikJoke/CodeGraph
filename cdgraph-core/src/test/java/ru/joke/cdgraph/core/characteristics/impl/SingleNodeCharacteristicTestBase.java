package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicComputationException;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class SingleNodeCharacteristicTestBase<T> {

    @Test
    public void testWhenNodeNotFoundThenException() throws Exception {
        final var characteristic = createCharacteristic(new SingleModuleCharacteristicParameters("test"));

        final var codeGraph = createCodeGraph();
        assertThrows(CodeGraphCharacteristicComputationException.class, () -> characteristic.compute(codeGraph));
    }

    @Nonnull
    protected abstract CodeGraphCharacteristic<T> createCharacteristic(@Nonnull SingleModuleCharacteristicParameters parameters);

    @Nonnull
    protected abstract CodeGraph createCodeGraph() throws Exception;
}
