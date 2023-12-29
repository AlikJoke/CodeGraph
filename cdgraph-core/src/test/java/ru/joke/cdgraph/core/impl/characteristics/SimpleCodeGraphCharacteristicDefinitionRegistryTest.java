package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinition;
import ru.joke.cdgraph.core.CodeGraphCharacteristicDefinitionNotFoundException;
import ru.joke.cdgraph.core.impl.characteristics.factors.AbstractnessCharacteristicDefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleCodeGraphCharacteristicDefinitionRegistryTest {

    @Test
    public void testWhenDefinitionNotFoundById() {
        final var registry = new SimpleCodeGraphCharacteristicDefinitionRegistry();

        assertThrows(CodeGraphCharacteristicDefinitionNotFoundException.class, () -> registry.find("test1"));
    }

    @Test
    public void testWhenDefinitionRegisteredAndFound() {
        final var registry = new SimpleCodeGraphCharacteristicDefinitionRegistry();

        final CodeGraphCharacteristicDefinition<?, ?> definition = new AbstractnessCharacteristicDefinition();
        registry.register(definition);

        final var foundDefinition = registry.find(definition.id());
        assertEquals(foundDefinition, definition, "Definition must be equal");
    }

    @Test
    public void testWhenDefinitionsLoadedAndFound() {
        final var registry = new SimpleCodeGraphCharacteristicDefinitionRegistry();
        registry.register(new SPIBasedCodeGraphCharacteristicDefinitionsLoader());

        final CodeGraphCharacteristicDefinition<?, ?> testAbstractnessDef = new AbstractnessCharacteristicDefinition();
        final var foundDefinition = registry.find(testAbstractnessDef.id());

        assertEquals(foundDefinition.getClass(), testAbstractnessDef.getClass(), "Definition type must be equal");
    }
}
