package ru.joke.cdgraph.core.impl.sinks;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeGraphOutputFileSinkTest {

    @Test
    public void test() throws IOException {
        final String firstValue = "1";
        final String secondValue = "2";

        final var tempFilePath = Files.createTempFile(UUID.randomUUID().toString(), "");
        tempFilePath.toFile().deleteOnExit();

        try (final var sink = new CodeGraphOutputFileSink(tempFilePath)) {
            sink.write(firstValue);
            sink.write(secondValue);
        }

        final var lines = Files.readAllLines(tempFilePath, StandardCharsets.UTF_8);

        assertEquals(2, lines.size(), "Lines count must be equal");
        assertEquals(
                firstValue,
                lines.get(0),
                "First string output value must be equal"
        );
        assertEquals(
                secondValue,
                lines.get(1),
                "Second string output value must be equal"
        );
    }
}
