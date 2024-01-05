package ru.joke.cdgraph.core.characteristics.impl.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicComputationException;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraph;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

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
        assertThrows(CodeGraphCharacteristicComputationException.class, () -> characteristic.compute(codeGraph));
    }
}
