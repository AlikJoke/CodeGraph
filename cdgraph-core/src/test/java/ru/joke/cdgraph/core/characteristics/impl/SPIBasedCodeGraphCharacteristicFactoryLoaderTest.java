package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;
import ru.joke.cdgraph.core.characteristics.impl.bridges.DependencyBridgesCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.clusters.ModuleClusteringCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.counters.DependenciesCountCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.factors.AbstractnessCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.factors.AllModulesAbstractnessCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.factors.AllModulesStabilityCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.factors.StabilityCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.locations.AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.locations.ClassLocationsCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.locations.ConflictingDependenciesCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.locations.PackageLocationsCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.paths.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SPIBasedCodeGraphCharacteristicFactoryLoaderTest {

    private static final Set<Class<? extends Annotation>> factoriesDescriptorsClasses = Set.of(
            DependencyBridgesCharacteristicFactoryDescriptor.class, ModuleClusteringCharacteristicFactoryDescriptor.class,
            DependenciesCountCharacteristicFactoryDescriptor.class, AbstractnessCharacteristicFactoryDescriptor.class,
            AllModulesAbstractnessCharacteristicFactoryDescriptor.class, AllModulesStabilityCharacteristicFactoryDescriptor.class,
            StabilityCharacteristicFactoryDescriptor.class, AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor.class,
            ClassLocationsCharacteristicFactoryDescriptor.class, ConflictingDependenciesCharacteristicFactoryDescriptor.class,
            PackageLocationsCharacteristicFactoryDescriptor.class, AllPathsBetweenModulesCharacteristicFactoryDescriptor.class,
            ShortestPathBetweenModulesCharacteristicFactoryDescriptor.class, TransitiveChainsCharacteristicFactoryDescriptor.class,
            CodeGraphDescriptionCharacteristicFactoryDescriptor.class
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
        assertEquals(factoriesDescriptorsClasses, loadedFactoriesClasses, "Loaded definitions must be equal");
    }

    private Class<? extends Annotation> getAnnotationType(final Class<?> annotationClass) {
        return Arrays.stream(annotationClass.getAnnotations())
                        .map(Annotation::annotationType)
                        .filter(annotationType -> annotationType.isAnnotationPresent(CodeGraphCharacteristicFactoryDescriptor.class))
                        .findAny()
                        .orElseThrow(() -> new CodeGraphConfigurationException("Required descriptor annotation not found"));
    }
}
