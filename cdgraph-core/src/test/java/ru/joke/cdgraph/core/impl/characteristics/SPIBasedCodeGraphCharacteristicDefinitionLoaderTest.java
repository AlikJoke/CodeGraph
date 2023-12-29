package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.impl.characteristics.bridges.DependencyBridgesCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.clusters.ModuleClusteringCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.counters.DependenciesCountCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.factors.AbstractnessCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.factors.AllModulesAbstractnessCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.factors.AllModulesStabilityCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.factors.StabilityCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.locations.AllClassesDuplicatesLocationsCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.locations.ClassLocationsCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.locations.ConflictingDependenciesCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.locations.PackageLocationsCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.paths.AllPathsBetweenModulesCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.paths.ShortestPathBetweenModulesCharacteristicDefinition;
import ru.joke.cdgraph.core.impl.characteristics.paths.TransitiveChainsCharacteristicDefinition;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SPIBasedCodeGraphCharacteristicDefinitionLoaderTest {

    private static final Set<Class<?>> definitionsClasses = Set.of(
            DependencyBridgesCharacteristicDefinition.class, ModuleClusteringCharacteristicDefinition.class,
            DependenciesCountCharacteristicDefinition.class, AbstractnessCharacteristicDefinition.class,
            AllModulesAbstractnessCharacteristicDefinition.class, AllModulesStabilityCharacteristicDefinition.class,
            StabilityCharacteristicDefinition.class, AllClassesDuplicatesLocationsCharacteristicDefinition.class,
            ClassLocationsCharacteristicDefinition.class, ConflictingDependenciesCharacteristicDefinition.class,
            PackageLocationsCharacteristicDefinition.class, AllPathsBetweenModulesCharacteristicDefinition.class,
            ShortestPathBetweenModulesCharacteristicDefinition.class, TransitiveChainsCharacteristicDefinition.class
    );

    @Test
    public void test() {
        final var loader = new SPIBasedCodeGraphCharacteristicDefinitionsLoader();

        final var definitions = loader.load();
        assertNotNull(definitions, "Definitions must be not null");
        assertFalse(definitions.isEmpty(), "Definitions must be not empty");

        final var loadedDefinitionsClasses =
                definitions
                        .stream()
                        .map(definition -> definition.getClass())
                        .collect(Collectors.toSet());
        assertEquals(definitionsClasses, loadedDefinitionsClasses, "Loaded definitions must be equal");
    }
}
