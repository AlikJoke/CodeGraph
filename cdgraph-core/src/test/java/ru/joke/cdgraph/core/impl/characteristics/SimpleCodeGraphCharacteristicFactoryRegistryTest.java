package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryNotFoundException;
import ru.joke.cdgraph.core.impl.characteristics.factors.AbstractnessCharacteristicFactory;
import ru.joke.cdgraph.core.impl.characteristics.factors.AbstractnessCharacteristicFactoryHandle;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleCodeGraphCharacteristicFactoryRegistryTest {

    @Test
    public void testWhenDefinitionNotFoundById() {
        final var registry = new SimpleCodeGraphCharacteristicFactoryRegistry();

        assertThrows(CodeGraphCharacteristicFactoryNotFoundException.class, () -> registry.find("test1"));
    }

    @Test
    public void testWhenDefinitionRegisteredAndFound() {
        final var registry = new SimpleCodeGraphCharacteristicFactoryRegistry();

        final CodeGraphCharacteristicFactory<?, ?, ?> definition = new AbstractnessCharacteristicFactory();
        registry.register(definition);

        final var foundDefinition = registry.find("module.abstractness");
        assertEquals(foundDefinition, definition, "Definition must be equal");
        assertNotNull(registry.find(AbstractnessCharacteristicFactoryHandle.class), "Definition should be found by annotation");
    }

    @Test
    public void testWhenDefinitionsLoadedAndFound() {
        final var registry = new SimpleCodeGraphCharacteristicFactoryRegistry();
        registry.register(new SPIBasedCodeGraphCharacteristicFactoriesLoader());

        final CodeGraphCharacteristicFactory<?, ?, ?> testAbstractnessDef = new AbstractnessCharacteristicFactory();
        final var foundDefinition = registry.find("module.abstractness");

        assertEquals(foundDefinition.getClass(), testAbstractnessDef.getClass(), "Definition type must be equal");
        assertNotNull(registry.find(AbstractnessCharacteristicFactoryHandle.class), "Definition should be found by annotation");
    }
}
