package ru.joke.cdgraph.starter.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CodeGraphDataSourceTypeTest {

    @Test
    public void testWhenEnumValueExist() {
        for (var value : CodeGraphDataSourceType.values()) {
            assertEquals(value, CodeGraphDataSourceType.from(value.getAlias()), "Enum value must be equal");
        }
    }

    @Test
    public void testWhenEnumValueDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> CodeGraphDataSourceType.from("test"), "Exception must be thrown");
    }

    @Test
    public void testFactoryCreation() {
        final var factoryEar = CodeGraphDataSourceType.createFactory(CodeGraphDataSourceType.EAR, CodeGraphDataSourceType.JAR);
        assertNotNull(factoryEar, "Factory must be not null");

        final var factoryFatJar = CodeGraphDataSourceType.createFactory(CodeGraphDataSourceType.FAT_JAR, null);
        assertNotNull(factoryFatJar, "Factory must be not null");
    }
}
