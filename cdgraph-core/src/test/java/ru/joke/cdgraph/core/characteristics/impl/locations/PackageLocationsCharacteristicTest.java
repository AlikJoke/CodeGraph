package ru.joke.cdgraph.core.characteristics.impl.locations;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.graph.GraphNode;

import javax.annotation.Nonnull;
import java.net.URISyntaxException;
import java.util.Set;

import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_JAR_3_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.createCodeGraphByJar;

public class PackageLocationsCharacteristicTest extends LocationsCharacteristicTestBase {

    @Test
    public void test() throws URISyntaxException {
        final var codeGraph = createCodeGraphByJar(TEST_JAR_3_PATH);

        makeLocationsChecks("ru.joke.test", codeGraph, true);
        makeLocationsChecks("ru.joke", codeGraph, true);
        makeLocationsChecks("ru.joke.test2", codeGraph, true);
        makeLocationsChecks("ru.joke.test3", codeGraph, false);
    }

    @Nonnull
    @Override
    protected CodeGraphCharacteristic<Set<GraphNode>> createCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        return new PackageLocationsCharacteristic("1", parameters);
    }
}
