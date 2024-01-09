package ru.joke.cdgraph.core.characteristics.impl;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicDescriptionNotFoundException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryNotFoundException;
import ru.joke.cdgraph.core.characteristics.impl.factors.AbstractnessCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.factors.AbstractnessCharacteristicFactoryDescriptor;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleCodeGraphCharacteristicServiceTest {

    @Test
    public void testWhenDefinitionNotFoundById() {
        final var service = new SimpleCodeGraphCharacteristicService();

        assertThrows(CodeGraphCharacteristicFactoryNotFoundException.class, () -> service.findFactory("test1"));
        assertThrows(CodeGraphCharacteristicDescriptionNotFoundException.class, () -> service.findDescription("test1"));
    }

    @Test
    public void testWhenDefinitionRegisteredAndFound() {
        final var service = new SimpleCodeGraphCharacteristicService();

        final CodeGraphCharacteristicFactory<?, ?, ?> definition = new AbstractnessCharacteristicFactory();
        service.registerFactory(definition);

        assertEquals(1, service.findDescriptions().size(), "Descriptions count must be equal");
        makeFactoryChecks(service);
    }

    @Test
    public void testWhenDefinitionsLoadedAndFound() {
        final var service = new SimpleCodeGraphCharacteristicService();
        service.registerFactories(new SPIBasedCodeGraphCharacteristicFactoriesLoader());

        makeFactoryChecks(service);
    }

    private void makeFactoryChecks(final SimpleCodeGraphCharacteristicService service) {

        final String characteristicId = "module.abstractness";
        final var foundFactory = service.findFactory(characteristicId);
        final var factoryByAnnotation = service.findFactory(AbstractnessCharacteristicFactoryDescriptor.class);
        assertEquals(foundFactory, factoryByAnnotation);

        final var description = service.findDescription(characteristicId);
        assertEquals(characteristicId, description.factoryDescriptor().characteristicId(), "Characteristic id must be equal");
        assertEquals(1, description.parameters().size(), "Characteristic parameters count must be equal");

        final var moduleParameterDescription = description.parameters().iterator().next();
        assertEquals("module-id", moduleParameterDescription.id(), "Parameter id must be equal");
        assertFalse(moduleParameterDescription.description().isBlank(), "Parameter description must be not empty");
    }
}
