package ru.joke.cdgraph.core.characteristics.impl.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class ShortestPathBetweenModulesCharacteristicTest extends PathBetweenModulesCharacteristicTestBase<PathBetweenModules> {

    @Test
    public void testWhenNoPathExistBetweenNodes() {
        final var params = new PathBetweenModulesCharacteristicParameters(TEST_MODULE_1, SQL_MODULE);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Result object must be not null");
        assertTrue(result.get().relationsInPath().isEmpty(), "Relations path must be empty");
        assertTrue(result.get().modulesInPath().isEmpty(), "Nodes path must be empty");
    }

    @Test
    public void testWhenOnlyOnePathExist() {
        final var params = new PathBetweenModulesCharacteristicParameters(TEST_MODULE_3, TEST_MODULE_1);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Result object must be not null");

        final var relations = result.get().relationsInPath();
        assertEquals(2, relations.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations.get(0).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(0).target().id(), "Target module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(1).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_1, relations.get(1).target().id(), "Target module must be equal");

        final var nodes = result.get().modulesInPath();
        assertEquals(3, nodes.size(), "Nodes path size must be equal");

        assertEquals(TEST_MODULE_3, nodes.get(0).id(), "First module must be equal");
        assertEquals(TEST_MODULE_2, nodes.get(1).id(), "Second module must be equal");
        assertEquals(TEST_MODULE_1, nodes.get(2).id(), "Third module must be equal");
    }

    @Test
    public void testWhenTwoPathsExistThenFoundShortest() {
        final var params = new PathBetweenModulesCharacteristicParameters(TEST_MODULE_3, SQL_MODULE);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Result object must be not null");

        final var relations = result.get().relationsInPath();
        assertEquals(1, relations.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations.getFirst().source().id(), "Source module must be equal");
        assertEquals(SQL_MODULE, relations.getFirst().target().id(), "Target module must be equal");

        final var nodes = result.get().modulesInPath();
        assertEquals(2, nodes.size(), "Nodes path size must be equal");

        assertEquals(TEST_MODULE_3, nodes.get(0).id(), "First module must be equal");
        assertEquals(SQL_MODULE, nodes.get(1).id(), "Second module must be equal");
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<PathBetweenModules> createCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        return new ShortestPathBetweenModulesCharacteristic("1", parameters);
    }
}
