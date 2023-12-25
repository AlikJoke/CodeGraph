package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;
import ru.joke.cdgraph.core.impl.characteristics.factors.AbstractnessCharacteristic;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphJarDataSource;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.impl.util.TestUtil.*;

public class AbstractnessCharacteristicTest {

    @Test
    public void testWhenNodeNotFoundThenException() {
        final var characteristic = new AbstractnessCharacteristic(new SingleNodeCharacteristicParameters("test"));

        final var codeGraph = new JavaModuleCodeGraph(createCodeGraphDatasource(TEST_MODULE_2_PATH));
        assertThrows(CodeGraphComputationException.class, () -> characteristic.compute(codeGraph));
    }

    @Test
    public void testAbstractness() throws URISyntaxException {
        final var jarUrl = getClass().getResource(TEST_JAR_PATH);
        final var codeGraph = new JavaModuleCodeGraph(new CodeGraphJarDataSource(Path.of(jarUrl.toURI()), new JarClassesMetadataReader()));

        final var params = new SingleNodeCharacteristicParameters(TEST_MODULE_3);
        final var characteristic = new AbstractnessCharacteristic(params);
        final var result = characteristic.compute(codeGraph);

        assertNotNull(result.get(), "Abstractness factor object must be not null");
        assertEquals(3.0 / 7, result.get().factor(), "Abstractness factor must be equal");
    }
}
