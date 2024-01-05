package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.clusters.ModuleClusteringCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.factors.AbstractnessCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.factors.AllModulesAbstractnessCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.locations.ClassLocationsCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.paths.ShortestPathBetweenModulesCharacteristicFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleCodeGraphCharacteristicParametersFactoryTest {

    @Test
    public void testNoRequiredParameters() {
        final var factory = new SimpleCodeGraphCharacteristicParametersFactory();
        assertThrows(CodeGraphCharacteristicConfigurationException.class, () -> factory.createFor(new AbstractnessCharacteristicFactory(), Map.of("some-param", "test")));
    }

    @Test
    public void testEmptyParameters() {
        final var factory = new SimpleCodeGraphCharacteristicParametersFactory();

        final var params = factory.createFor(
                new AllModulesAbstractnessCharacteristicFactory(),
                Map.of("1", "2")
        );
        assertNotNull(params, "Created parameters must be not null");
        assertEquals(CodeGraphCharacteristicParameters.createEmpty(), params, "Created parameters must be empty");
    }

    @Test
    public void testModuleClusteringCharacteristicParameters() {
        final var factory = new SimpleCodeGraphCharacteristicParametersFactory();

        final String optimizingFactor = "3";
        final String maxRounds = "2";
        final var params = factory.createFor(
                new ModuleClusteringCharacteristicFactory(),
                Map.of("optimizing-factor", optimizingFactor, "max-rounds", maxRounds)
        );
        assertNotNull(params, "Created parameters must be not null");
        assertEquals(Integer.parseInt(optimizingFactor), params.optimizingFactor(), "Value from parameters must be equal");
        assertEquals(Integer.parseInt(maxRounds), params.maxRounds(), "Value from parameters must be equal");
    }

    @Test
    public void testPathBetweenModulesCharacteristicParameters() {
        final var factory = new SimpleCodeGraphCharacteristicParametersFactory();

        final String sourceModule = "test1";
        final String targetModule = "test2";
        final var params = factory.createFor(
                new ShortestPathBetweenModulesCharacteristicFactory(),
                Map.of("source-module-id", sourceModule, "target-module-id", targetModule)
        );
        assertNotNull(params, "Created parameters must be not null");
        assertEquals(sourceModule, params.sourceModuleId(), "Value from parameters must be equal");
        assertEquals(targetModule, params.targetModuleId(), "Value from parameters must be equal");
    }

    @Test
    public void testResourceLocationsCharacteristicParameters() {
        final var factory = new SimpleCodeGraphCharacteristicParametersFactory();

        final String className = "ru.test.Test";
        final var params = factory.createFor(new ClassLocationsCharacteristicFactory(), Map.of("resource-name", className));
        assertNotNull(params, "Created parameters must be not null");
        assertEquals(className, params.resourceName(), "Value from parameters must be equal");
    }

    @Test
    public void testSingleModuleCharacteristicParameters() {
        final var factory = new SimpleCodeGraphCharacteristicParametersFactory();

        final String moduleId = "test";
        final var params = factory.createFor(new AbstractnessCharacteristicFactory(), Map.of("module-id", moduleId));
        assertNotNull(params, "Created parameters must be not null");
        assertEquals(moduleId, params.moduleId(), "Value from parameters must be equal");
    }
}
