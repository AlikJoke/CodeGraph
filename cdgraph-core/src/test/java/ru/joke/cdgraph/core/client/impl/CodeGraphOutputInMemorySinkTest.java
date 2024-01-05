package ru.joke.cdgraph.core.client.impl;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeGraphOutputInMemorySinkTest {

    @Test
    public void test() {
        final String firstValue = "1";
        final String secondValue = "2";

        final List<String> result = new ArrayList<>();
        try (final var sink = new CodeGraphOutputInMemorySink(result)) {
            sink.write(firstValue);
            sink.write(secondValue);
        }

        assertEquals(2, result.size(), "Count of items must be equal");
        assertEquals(firstValue, result.get(0), "First item must be equal");
        assertEquals(secondValue, result.get(1), "Second item must be equal");
    }
}
