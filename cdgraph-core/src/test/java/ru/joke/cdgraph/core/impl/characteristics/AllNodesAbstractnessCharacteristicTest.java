package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;
import ru.joke.cdgraph.core.impl.characteristics.factors.AllNodesAbstractnessCharacteristic;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphJarDataSource;
import ru.joke.cdgraph.core.impl.jms.JavaModuleCodeGraph;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_JAR_PATH;
import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_MODULE_3;

public class AllNodesAbstractnessCharacteristicTest {

    @Test
    public void test() throws URISyntaxException {
        final var jarUrl = getClass().getResource(TEST_JAR_PATH);
        final var codeGraph = new JavaModuleCodeGraph(new CodeGraphJarDataSource(Path.of(jarUrl.toURI()), new JarClassesMetadataReader()));

        final var characteristic = new AllNodesAbstractnessCharacteristic();
        final var result = characteristic.compute(codeGraph);

        final var resultMap = result.get();
        assertNotNull(resultMap, "Abstractness map must be not null");
        assertEquals(1, resultMap.size(), "Abstractness map size must be equal");

        final var factor = resultMap.get(TEST_MODULE_3);
        assertNotNull(factor, "Abstractness factor for module must be not null");
        assertEquals(3.0 / 7, factor.factor(), "Abstractness factor must be equal");
    }
}
