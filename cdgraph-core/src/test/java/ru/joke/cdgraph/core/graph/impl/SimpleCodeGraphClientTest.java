package ru.joke.cdgraph.core.graph.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.impl.SPIBasedCodeGraphCharacteristicFactoriesLoader;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicFactoryRegistry;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.paths.PathBetweenModulesCharacteristicParameters;
import ru.joke.cdgraph.core.client.impl.SimpleCodeGraphClient;
import ru.joke.cdgraph.core.client.impl.SimpleCodeGraphRequest;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;
import ru.joke.cdgraph.core.datasources.impl.CodeGraphJarDataSourceFactory;
import ru.joke.cdgraph.core.graph.impl.jpms.JavaModuleCodeGraphFactory;
import ru.joke.cdgraph.core.client.impl.CodeGraphOutputInMemorySink;
import ru.joke.cdgraph.core.client.impl.SimpleCodeGraphOutputSpecification;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.joke.cdgraph.core.test.util.TestUtil.*;

public class SimpleCodeGraphClientTest {

    @Test
    public void test() throws URISyntaxException {

        final var graph = createGraph();
        final var requiredCharacteristics = getRequiredCharacteristics();

        final List<String> characteristicsResultList = new ArrayList<>();
        try (final var sink = new CodeGraphOutputInMemorySink(characteristicsResultList)) {
            final var outputSpecification = new SimpleCodeGraphOutputSpecification(CodeGraphOutputSpecification.Format.JSON, sink);

            final var graphRequest = new SimpleCodeGraphRequest(graph, requiredCharacteristics);

            final var client = new SimpleCodeGraphClient();
            client.execute(graphRequest, outputSpecification);
        }

        assertEquals(2, characteristicsResultList.size(), "Characteristics results count must be equal");
    }

    private CodeGraph createGraph() throws URISyntaxException {
        final var codeGraphFactory = new JavaModuleCodeGraphFactory();

        final var dataSource = createDataSource();
        return codeGraphFactory.create(dataSource);
    }

    private CodeGraphDataSource createDataSource() throws URISyntaxException {
        final var codeGraphDataSourceFactory = new CodeGraphJarDataSourceFactory();

        final var jarPath = getJarFile(TEST_JAR_3_PATH).toPath();
        return codeGraphDataSourceFactory.create(jarPath);
    }

    private List<CodeGraphCharacteristic<?>> getRequiredCharacteristics() {
        final var registry = new SimpleCodeGraphCharacteristicFactoryRegistry();
        registry.register(new SPIBasedCodeGraphCharacteristicFactoriesLoader());

        final var abstractnessDef = registry.find("module.abstractness");
        final var abstractnessCharacteristic = abstractnessDef.createCharacteristic(new SingleModuleCharacteristicParameters(TEST_MODULE_3));

        final var shortestPathDef = registry.find("shortest.path.between.modules");
        final var shortestPathCharacteristic = shortestPathDef.createCharacteristic(new PathBetweenModulesCharacteristicParameters(TEST_MODULE_3, BASE_MODULE));

        return List.of(abstractnessCharacteristic, shortestPathCharacteristic);
    }
}
