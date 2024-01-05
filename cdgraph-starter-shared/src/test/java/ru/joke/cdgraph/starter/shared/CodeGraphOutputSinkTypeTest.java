package ru.joke.cdgraph.starter.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CodeGraphOutputSinkTypeTest {

    @Test
    public void testWhenEnumValueExist() {
        assertEquals(CodeGraphOutputSinkType.FILE, CodeGraphOutputSinkType.from(CodeGraphOutputSinkType.FILE.getAlias()), "Enum value must be equal");
        assertEquals(CodeGraphOutputSinkType.CONSOLE, CodeGraphOutputSinkType.from(CodeGraphOutputSinkType.CONSOLE.getAlias()), "Enum value must be equal");
    }

    @Test
    public void testWhenEnumValueDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> CodeGraphOutputSinkType.from("1"), "Exception must be thrown");
    }
}
