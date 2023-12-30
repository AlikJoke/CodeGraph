package ru.joke.cdgraph.core.impl.characteristics.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristic;

import javax.annotation.Nonnull;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class AllPathsBetweenModulesCharacteristicTest extends PathBetweenModulesCharacteristicTestBase<List<PathBetweenModules>> {

    @Test
    public void testWhenNoPathExistBetweenNodes() {
        final var params = new PathBetweenModulesCharacteristicParameters(TEST_MODULE_1, SQL_MODULE);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH);
        final var result = characteristic.compute(codeGraph);

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertTrue(paths.isEmpty(), "Paths must be empty");
    }

    @Test
    public void testWhenOnlyOnePathExist() {
        final var params = new PathBetweenModulesCharacteristicParameters(TEST_MODULE_3, TEST_MODULE_1);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(codeGraph);

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(1, paths.size(), "Should exist only 1 path");

        final var relations = paths.get(0).relationsInPath();
        assertEquals(2, relations.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations.get(0).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(0).target().id(), "Target module must be equal");
        assertEquals(TEST_MODULE_2, relations.get(1).source().id(), "Source module must be equal");
        assertEquals(TEST_MODULE_1, relations.get(1).target().id(), "Target module must be equal");

        final var nodes = paths.get(0).modulesInPath();
        assertEquals(3, nodes.size(), "Nodes path size must be equal");

        assertEquals(TEST_MODULE_3, nodes.get(0).id(), "First module must be equal");
        assertEquals(TEST_MODULE_2, nodes.get(1).id(), "Second module must be equal");
        assertEquals(TEST_MODULE_1, nodes.get(2).id(), "Third module must be equal");
    }

    @Test
    public void testWhenMultiplePathsExists() {
        final var params = new PathBetweenModulesCharacteristicParameters(TEST_MODULE_3, BASE_MODULE);
        final var characteristic = createCharacteristic(params);

        final var codeGraph = createCodeGraph(TEST_MODULE_1_PATH, TEST_MODULE_2_PATH, TEST_MODULE_3_PATH);
        final var result = characteristic.compute(codeGraph);

        final var paths = result.get();
        assertNotNull(paths, "Result object must be not null");
        assertEquals(11, paths.size(), "Should exist 5 paths");

        final PathBetweenModules path1 = paths.get(0);

        final var relations1 = path1.relationsInPath();
        assertEquals(1, relations1.size(), "Relations path size must be equal");

        assertEquals(TEST_MODULE_3, relations1.get(0).source().id(), "Source module must be equal");
        assertEquals(BASE_MODULE, relations1.get(0).target().id(), "Target module must be equal");

        final var nodes1 = path1.modulesInPath();
        assertEquals(2, nodes1.size(), "Nodes path size must be equal");

        final PathBetweenModules path2 = paths.get(1);

        final var relations2 = path2.relationsInPath();
        assertEquals(2, relations2.size(), "Relations path size must be equal");

        final PathBetweenModules path3 = paths.get(2);

        final var relations3 = path3.relationsInPath();
        assertEquals(2, relations3.size(), "Relations path size must be equal");

        makeEqualLengthPathsChecks(paths, new int[] { 3, 4, 5, 6, 7 }, 3);
        makeEqualLengthPathsChecks(paths, new int[] { 8, 9, 10 }, 4);
    }

    private void makeEqualLengthPathsChecks(
            final List<PathBetweenModules> paths,
            final int[] equalLengthPathsIndexes,
            final int expectedRelationsLength) {

        for (int i : equalLengthPathsIndexes) {
            assertEquals(expectedRelationsLength, paths.get(i).relationsInPath().size(), "Relations path size must be equal");
            assertEquals(expectedRelationsLength + 1, paths.get(i).modulesInPath().size(), "Nodes in path size must be equal");
        }
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<List<PathBetweenModules>> createCharacteristic(@Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        return new AllPathsBetweenModulesCharacteristic("1", parameters);
    }
}
