package ru.joke.cdgraph.core.characteristics.impl.locations;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.graph.GraphNode;

import javax.annotation.Nonnull;
import java.net.URISyntaxException;
import java.util.Set;

import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_JAR_3_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.createCodeGraphByJar;

public class ClassLocationsCharacteristicTest extends LocationsCharacteristicTestBase {

    @Test
    public void test() throws URISyntaxException {
        final var codeGraph = createCodeGraphByJar(TEST_JAR_3_PATH);

        makeLocationsChecks("ru.joke.test.TestInterface", codeGraph, true);
        makeLocationsChecks("ru.joke.test.NotExistingTestClass", codeGraph, false);
        makeLocationsChecks("ru.joke.test2.TestFinalClass", codeGraph, true);
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<Set<GraphNode>> createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new ClassLocationsCharacteristic("1", parameters);
    }
}
