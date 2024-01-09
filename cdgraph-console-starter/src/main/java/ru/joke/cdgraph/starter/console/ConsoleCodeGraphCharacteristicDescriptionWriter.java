package ru.joke.cdgraph.starter.console;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicDescription;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameter;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.impl.SPIBasedCodeGraphCharacteristicFactoriesLoader;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicService;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

final class ConsoleCodeGraphCharacteristicDescriptionWriter {

    private static final String PARAMETER_TEMPLATE = "'%s': {'required': %s; 'description': '%s'}";

    private final CodeGraphCharacteristicService characteristicsService;

    ConsoleCodeGraphCharacteristicDescriptionWriter() {
        this.characteristicsService = new SimpleCodeGraphCharacteristicService();
        this.characteristicsService.registerFactories(new SPIBasedCodeGraphCharacteristicFactoriesLoader());
    }

    void writeAll() {
        final var sb = new StringBuilder();
        final var descriptions = this.characteristicsService.findDescriptions();
        descriptions.forEach(description -> fillCharacteristicDescription(sb, description));

        System.out.println(sb);
    }

    void write(@Nonnull String characteristicId) {
        final var description = this.characteristicsService.findDescription(characteristicId);
        final var sb = new StringBuilder();
        fillCharacteristicDescription(sb, description);

        System.out.println(sb);
    }

    private void fillCharacteristicDescription(final StringBuilder sb, final CodeGraphCharacteristicDescription description) {
        sb.append("Characteristic: '")
                .append(description.factoryDescriptor().characteristicId())
                .append("'\n");

        final var parametersInfo =
                description.parameters()
                            .stream()
                            .map(this::createCharacteristicParameterDescription)
                            .collect(Collectors.joining("\n"));

        if (!parametersInfo.isBlank()) {
            sb.append("Parameters: \n");
        }

        sb.append(parametersInfo).append("\n");
    }

    private String createCharacteristicParameterDescription(final CodeGraphCharacteristicParameter parameter) {
        return PARAMETER_TEMPLATE.formatted(parameter.id(), parameter.required(), parameter.description());
    }
}
