package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.CodeGraphConfigurationException;
import ru.joke.cdgraph.core.impl.characteristics.bridges.DependencyBridgesCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.clusters.ModuleClusteringCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.counters.DependenciesCountCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.factors.AbstractnessCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.factors.AllModulesAbstractnessCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.factors.AllModulesStabilityCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.factors.StabilityCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.locations.AllClassesDuplicatesLocationsCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.locations.ClassLocationsCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.locations.ConflictingDependenciesCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.locations.PackageLocationsCharacteristicFactoryHandle;
import ru.joke.cdgraph.core.impl.characteristics.paths.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SPIBasedCodeGraphCharacteristicFactoryLoaderTest {

    private static final Set<Class<? extends Annotation>> factoriesHandlesClasses = Set.of(
            DependencyBridgesCharacteristicFactoryHandle.class, ModuleClusteringCharacteristicFactoryHandle.class,
            DependenciesCountCharacteristicFactoryHandle.class, AbstractnessCharacteristicFactoryHandle.class,
            AllModulesAbstractnessCharacteristicFactoryHandle.class, AllModulesStabilityCharacteristicFactoryHandle.class,
            StabilityCharacteristicFactoryHandle.class, AllClassesDuplicatesLocationsCharacteristicFactoryHandle.class,
            ClassLocationsCharacteristicFactoryHandle.class, ConflictingDependenciesCharacteristicFactoryHandle.class,
            PackageLocationsCharacteristicFactoryHandle.class, AllPathsBetweenModulesCharacteristicFactoryHandle.class,
            ShortestPathBetweenModulesCharacteristicFactoryHandle.class, TransitiveChainsCharacteristicFactoryHandle.class,
            CodeGraphDescriptionCharacteristicFactoryHandle.class
    );

    @Test
    public void test() {
        final var loader = new SPIBasedCodeGraphCharacteristicFactoriesLoader();

        final var factories = loader.load();
        assertNotNull(factories, "Definitions must be not null");
        assertFalse(factories.isEmpty(), "Definitions must be not empty");

        final var loadedFactoriesClasses =
                factories
                        .stream()
                        .map(factory -> factory.getClass())
                        .map(this::getAnnotationType)
                        .collect(Collectors.toSet());
        assertEquals(factoriesHandlesClasses, loadedFactoriesClasses, "Loaded definitions must be equal");
    }

    private Class<? extends Annotation> getAnnotationType(final Class<?> annotationClass) {
        return Arrays.stream(annotationClass.getAnnotations())
                        .map(Annotation::annotationType)
                        .filter(annotationType -> annotationType.isAnnotationPresent(CodeGraphCharacteristicFactoryHandle.class))
                        .findAny()
                        .orElseThrow(() -> new CodeGraphConfigurationException("Required handle annotation not found"));
    }
}
