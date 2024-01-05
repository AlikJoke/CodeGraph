package ru.joke.cdgraph.starter.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CodeGraphTypeTest {

    @Test
    public void testWhenEnumValueExist() {
        assertEquals(CodeGraphType.JAVA_MODULES, CodeGraphType.from(CodeGraphType.JAVA_MODULES.getAlias()), "Enum value must be equal");
        assertEquals(CodeGraphType.MAVEN_MODULES, CodeGraphType.from(CodeGraphType.MAVEN_MODULES.getAlias()), "Enum value must be equal");
    }

    @Test
    public void testWhenEnumValueDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> CodeGraphType.from("java-modules1"), "Exception must be thrown");
    }
}
