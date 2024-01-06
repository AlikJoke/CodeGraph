package ru.joke.cdgraph.maven.params.eval;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.factors.Factor;
import ru.joke.cdgraph.core.characteristics.impl.locations.ConflictingDependencies;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CharacteristicResultSnippetBasedBoundCheckerTest {

    private final CharacteristicResultSnippetBasedBoundChecker checker = new CharacteristicResultSnippetBasedBoundChecker(new SystemStreamLog());

    @Test
    public void testFactor() {

        final Factor abstractness = new Factor(0.6);
        final var result = new SimpleCodeGraphCharacteristicResult<>("abstractness", abstractness);

        makeCheck(result, "result.factor() > 0.2", false);
        makeCheck(result, "result.factor() < 0.2", true);
    }

    @Test
    public void testMap() {

        final Map<String, Factor> factors = Map.of("1", new Factor(0.6), "2", new Factor(0.4));
        final var result = new SimpleCodeGraphCharacteristicResult<>("all.abstractness", factors);

        makeCheck(result, "result.values().stream().anyMatch(r -> r.factor() > 0.2)", false);
        makeCheck(result, "result.values().stream().anyMatch(r -> r.factor() > 0.8)", true);
    }

    @Test
    public void testList() {
        final List<Factor> factors = List.of(new Factor(0.6), new Factor(0.4));
        final var result = new SimpleCodeGraphCharacteristicResult<>("all.abstractness", factors);

        makeCheck(result, "result.stream().anyMatch(r -> r.factor() > 0.2)", false);
        makeCheck(result, "result.stream().anyMatch(r -> r.factor() > 0.8)", true);
    }

    @Test
    public void testConflictingDependenciesCountCheck() {
        final var conflictingModules = Set.of(createNode("test1-0.1"), createNode("test1-0.2"));
        final Set<ConflictingDependencies> dependencies = Set.of(new ConflictingDependencies(conflictingModules));
        final var result = new SimpleCodeGraphCharacteristicResult<>("conflicting.dependencies", dependencies);

        makeCheck(result, "!result.isEmpty()", false);
        makeCheck(result, "result.isEmpty()", true);
    }

    @Test
    public void testInvalidSnippet() {
        assertThrows(CodeGraphCharacteristicConfigurationException.class, () -> this.checker.check(new SimpleCodeGraphCharacteristicResult<>("1", 2), "result.isEmpty()"));
    }

    @Test
    public void testSnippetWithRuntimeException() {
        assertThrows(CodeGraphCharacteristicConfigurationException.class, () -> this.checker.check(new SimpleCodeGraphCharacteristicResult<>("1", 2), "(result / 0) > 0"));
    }

    private void makeCheck(final CodeGraphCharacteristicResult<?> result, final String boundExpression, final boolean negate) {
        assertTrue(negate != checker.check(result, boundExpression), "Result of the check must be equal");
    }

    private GraphNode createNode(final String id) {
        return new SimpleGraphNode(id, Collections.emptySet(), Collections.emptyMap());
    }
}
