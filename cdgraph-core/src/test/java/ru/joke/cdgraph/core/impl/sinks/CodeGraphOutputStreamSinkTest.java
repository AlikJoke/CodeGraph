package ru.joke.cdgraph.core.impl.sinks;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeGraphOutputStreamSinkTest {

    @Test
    public void test() {
        final String firstValue = "1";
        final String secondValue = "2";

        final var outputStream = new ByteArrayOutputStream();
        try (final var stream = new PrintStream(outputStream);
                final var sink = new CodeGraphOutputStreamSink(stream)) {
            sink.write(firstValue);
            sink.write(secondValue);
        }

        assertEquals(
                firstValue + System.lineSeparator() + secondValue + System.lineSeparator(),
                outputStream.toString(StandardCharsets.UTF_8),
                "String output value must be equal"
        );
    }
}
