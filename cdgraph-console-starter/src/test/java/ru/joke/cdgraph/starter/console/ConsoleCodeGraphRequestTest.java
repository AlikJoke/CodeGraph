package ru.joke.cdgraph.starter.console;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.test.util.TestUtil;
import ru.joke.cdgraph.starter.shared.CodeGraphDataSourceType;
import ru.joke.cdgraph.starter.shared.CodeGraphType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_JAR_3_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_MODULE_3;
import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.*;

public class ConsoleCodeGraphRequestTest {

    private static final String CHARACTERISTIC = "module.abstractness:module-id=test;all.modules.stability";

    @Test
    public void testNoCache() throws IOException {
        final File jarFile = createFile();
        final var request = new ConsoleCodeGraphRequest(createArgs(jarFile, false));

        assertNotNull(request.requiredCharacteristics(), "Characteristics must be not null");
        assertEquals(2, request.requiredCharacteristics().size(), "Characteristics count must be equal");
        assertNotNull(request.codeGraph(), "CodeGraph must be not null");
        assertEquals(TEST_MODULE_3, request.codeGraph().findRootNode().id());
    }

    @Test
    public void testCachedGraph() throws IOException {
        final var jarFile = createFile();
        final var request1 = new ConsoleCodeGraphRequest(createArgs(jarFile, true));
        assertNotNull(request1.codeGraph(), "CodeGraph must be not null");

        final var request2 = new ConsoleCodeGraphRequest(createArgs(jarFile, true));
        assertSame(request1.codeGraph(), request2.codeGraph(), "CodeGraph must be same");
    }

    private File createFile() throws IOException {
        final File jarFile = File.createTempFile(UUID.randomUUID().toString(), "");
        jarFile.deleteOnExit();

        try (final var jarStream = TestUtil.class.getResourceAsStream(TEST_JAR_3_PATH);
                final FileOutputStream fos = new FileOutputStream(jarFile)) {
            fos.write(Objects.requireNonNull(jarStream, "jarStream").readAllBytes());
        }

        return jarFile;
    }

    private Map<String, String> createArgs(File jarFile, boolean enableCaching) {
        return Map.of(
                DATA_PATH, jarFile.getAbsolutePath(),
                GRAPH_TYPE, CodeGraphType.JAVA_MODULES.getAlias(),
                DATASOURCE_TYPE, CodeGraphDataSourceType.JAR.getAlias(),
                CHARACTERISTICS, CHARACTERISTIC,
                CACHE_GRAPH, String.valueOf(enableCaching)
        );
    }
}
