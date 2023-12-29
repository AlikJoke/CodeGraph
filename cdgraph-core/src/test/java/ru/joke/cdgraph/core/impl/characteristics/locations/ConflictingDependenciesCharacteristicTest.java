package ru.joke.cdgraph.core.impl.characteristics.locations;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphTag;
import ru.joke.cdgraph.core.impl.SimpleGraphNode;
import ru.joke.cdgraph.core.impl.SimpleGraphTag;

import javax.annotation.Nonnull;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.AbstractCodeGraph.VERSION_TAG;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class ConflictingDependenciesCharacteristicTest {

    private static final String ARTIFACT1_ID = "ru.joke.test:test1";
    private static final String ARTIFACT2_ID = "ru.joke.test:test2";
    private static final String VERSION_1 = "1.0";
    private static final String VERSION_1_1 = "1.1";

    @Test
    public void testWhenNoConflictingDependencies() throws URISyntaxException {
        final CodeGraph codeGraph = createCodeGraphByJar(TEST_JAR_3_PATH, TEST_JAR_2_PATH);
        final ConflictingDependenciesCharacteristic characteristic = new ConflictingDependenciesCharacteristic();

        final var result = characteristic.compute(codeGraph);
        assertNotNull(result.get(), "Result of computing must be not null");
        assertTrue(result.get().isEmpty(), "Conflicting dependencies must be empty");
    }

    @Test
    public void testWhenExistsConflictingDependencies() {
        final CodeGraph codeGraph = buildCodeGraph();
        final ConflictingDependenciesCharacteristic characteristic = new ConflictingDependenciesCharacteristic();

        final var result = characteristic.compute(codeGraph);
        final Set<ConflictingDependencies> allConflictingDependencies = result.get();

        assertNotNull(allConflictingDependencies, "Result of computing must be not null");
        assertEquals(1, allConflictingDependencies.size(), "Conflicting dependencies groups count must be equal");

        final var oneArtifactConflictingDependencies = allConflictingDependencies.iterator().next();
        final var conflictingDependencies = oneArtifactConflictingDependencies.conflictingDependencies();

        assertEquals(2, conflictingDependencies.size(), "Conflicting dependencies count must be equal");

        final var node1 = codeGraph.findNodeById(createFullArtifactId(ARTIFACT1_ID, VERSION_1)).orElseThrow();
        final var node2 = codeGraph.findNodeById(createFullArtifactId(ARTIFACT1_ID, VERSION_1_1)).orElseThrow();

        assertTrue(conflictingDependencies.contains(node1), "Conflicting dependencies must contain node: " + node1.id());
        assertTrue(conflictingDependencies.contains(node2), "Conflicting dependencies must contain node: " + node2.id());
    }

    private String createFullArtifactId(final String id, final String version) {
        return id + ":" + version;
    }

    private CodeGraph buildCodeGraph() {
        final GraphNode node1 = createGraphNode(ARTIFACT1_ID, VERSION_1);
        final GraphNode node2 = createGraphNode(ARTIFACT1_ID, VERSION_1_1);
        final GraphNode node3 = createGraphNode(ARTIFACT2_ID, VERSION_1);

        final Map<String, GraphNode> nodesMap = Map.of(node1.id(), node1, node2.id(), node2, node3.id(), node3);

        return new CodeGraph() {
            @Nonnull
            @Override
            public GraphNode findRootNode() {
                throw new UnsupportedOperationException();
            }

            @Nonnull
            @Override
            public Optional<GraphNode> findNodeById(@Nonnull String id) {
                return Optional.ofNullable(nodesMap.get(id));
            }

            @Nonnull
            @Override
            public Collection<GraphNode> findAllNodes() {
                return nodesMap.values();
            }
        };
    }

    private GraphNode createGraphNode(final String artifactId, final String version) {
        return new SimpleGraphNode(createFullArtifactId(artifactId, version), Collections.emptySet(), createGraphTagsMap(version));
    }

    private Map<String, GraphTag<?>> createGraphTagsMap(final String version) {
        return Map.of(VERSION_TAG, new SimpleGraphTag<>(VERSION_TAG, version));
    }
}
