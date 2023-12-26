package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphComputationException;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class SingleNodeCharacteristicTestBase<T> {

    @Test
    public void testWhenNodeNotFoundThenException() throws Exception {
        final var characteristic = createCharacteristic(new SingleNodeCharacteristicParameters("test"));

        final var codeGraph = createCodeGraph();
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }

    @Nonnull
    protected abstract CodeGraphCharacteristic<T> createCharacteristic(@Nonnull SingleNodeCharacteristicParameters parameters);

    @Nonnull
    protected abstract CodeGraph createCodeGraph() throws Exception;
}
