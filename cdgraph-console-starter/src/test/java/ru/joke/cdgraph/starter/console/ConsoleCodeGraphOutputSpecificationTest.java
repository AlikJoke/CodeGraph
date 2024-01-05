package ru.joke.cdgraph.starter.console;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;
import ru.joke.cdgraph.core.client.impl.CodeGraphOutputFileSink;
import ru.joke.cdgraph.core.client.impl.CodeGraphOutputStreamSink;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.*;

public class ConsoleCodeGraphOutputSpecificationTest {

    @Test
    public void testConsoleOutputSpecification() {
        makeConsoleOutputSpecificationChecks(CodeGraphOutputSpecification.Format.JSON);
        makeConsoleOutputSpecificationChecks(CodeGraphOutputSpecification.Format.PLAIN_TEXT);
    }

    @Test
    public void testFileOutputSpecification() throws IOException {
        makeFileOutputSpecificationChecks(CodeGraphOutputSpecification.Format.JSON);
        makeFileOutputSpecificationChecks(CodeGraphOutputSpecification.Format.PLAIN_TEXT);
    }

    @Test
    public void testIncorrectFormat() {
        final Map<String, String> params = Map.of(OUTPUT_FORMAT, "xml", SINK_TYPE, "console");
        final var specification = new ConsoleCodeGraphOutputSpecification(params);

        assertThrows(IllegalArgumentException.class, specification::format, "Incorrect format provided");
    }

    @Test
    public void testUnsupportedSink() {
        final Map<String, String> params = Map.of(OUTPUT_FORMAT, CodeGraphOutputSpecification.Format.JSON.getAlias(), SINK_TYPE, "database");
        final var specification = new ConsoleCodeGraphOutputSpecification(params);

        assertThrows(IllegalArgumentException.class, specification::sink, "Incorrect sink provided");
    }

    private void makeConsoleOutputSpecificationChecks(final CodeGraphOutputSpecification.Format format) {
        final Map<String, String> params = Map.of(OUTPUT_FORMAT, format.getAlias(), SINK_TYPE, "console");
        final var specification = new ConsoleCodeGraphOutputSpecification(params);

        assertEquals(format, specification.format(), "Format must be equal");
        assertInstanceOf(CodeGraphOutputStreamSink.class, specification.sink(), "Sink must be instanceof Stream sink");
    }

    private void makeFileOutputSpecificationChecks(final CodeGraphOutputSpecification.Format format) throws IOException {
        final File tempFile = File.createTempFile(UUID.randomUUID().toString(), "");
        tempFile.deleteOnExit();

        final Map<String, String> params = Map.of(
                OUTPUT_FORMAT, format.getAlias(),
                SINK_TYPE, "file",
                OUTPUT_FILE_PATH, tempFile.getAbsolutePath()
        );
        final var specification = new ConsoleCodeGraphOutputSpecification(params);

        assertEquals(format, specification.format(), "Format must be equal");
        assertInstanceOf(CodeGraphOutputFileSink.class, specification.sink(), "Sink must be instanceof File sink");
    }
}
