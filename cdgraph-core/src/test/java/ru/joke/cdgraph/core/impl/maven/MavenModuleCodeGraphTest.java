package ru.joke.cdgraph.core.impl.maven;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphEarDataSource;
import ru.joke.cdgraph.core.impl.datasources.CodeGraphWarDataSourceFactory;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MavenModuleCodeGraphTest {

    private static final String TEST_EAR_EXT_PATH = "/ds/test-modules-ext.ear";
    private static final String TEST_EAR_ID = "ru.joke.cdgraph.test-modules:test-modules4:1.0-SNAPSHOT";
    private static final String TEST_WAR_ID = "ru.joke.cdgraph.test-modules:test-modules3:1.0-SNAPSHOT";

    @Test
    public void test() throws URISyntaxException {
        final var testEarUrl = getClass().getResource(TEST_EAR_EXT_PATH);
        final var path = Path.of(testEarUrl.toURI());
        final var ds = new CodeGraphEarDataSource(path, new JarClassesMetadataReader(), new CodeGraphWarDataSourceFactory());

        final var codeGraph = new MavenModuleCodeGraphFactory().create(ds);

        assertNotNull(codeGraph, "CodeGraph must be not null");

        final var rootNode = codeGraph.findRootNode();
        assertNotNull(rootNode, "CodeGraph root node must be not null");
        assertEquals(TEST_EAR_ID, rootNode.id(), "Root node must be ear");
        assertEquals(1, rootNode.relations().size(), "Relations size of ear module must be equal");
        assertEquals(8, codeGraph.findAllNodes().size(), "CodeGraph nodes size must be equal");

        final var relationToWar = rootNode.relations().iterator().next();
        assertEquals(rootNode, relationToWar.source(), "Source of relation must be equal");
        assertEquals(TEST_WAR_ID, relationToWar.target().id(), "Target of relation must be equal");

        // TODO other checks
    }
}
