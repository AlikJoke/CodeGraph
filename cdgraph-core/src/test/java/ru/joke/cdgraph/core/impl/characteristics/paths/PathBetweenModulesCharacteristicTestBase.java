package ru.joke.cdgraph.core.impl.characteristics.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

abstract class PathBetweenModulesCharacteristicTestBase<T> {

    @Test
    public void testWhenSourceNodeNotFoundThenException() {
        testWhenOneOfNodeNotFound("test", SQL_MODULE);
    }

    @Test
    public void testWhenTargetNodeNotFoundThenException() {
        testWhenOneOfNodeNotFound(SQL_MODULE, "test");
    }

    @Nonnull
    protected abstract CodeGraphCharacteristic<T> createCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters);

    @Nonnull
    protected CodeGraph createCodeGraph(String... modules) {
        return new JavaModuleCodeGraph(createCodeGraphDatasource(modules));
    }

    private void testWhenOneOfNodeNotFound(final String sourceId, final String targetId) {
        final var params = new PathBetweenModulesCharacteristicParameters(sourceId, targetId);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH);
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }
}
