package ru.joke.cdgraph.starter.console.params;

import javax.annotation.Nonnull;
import java.util.Map;

public final class ConsoleParametersProcessor {

    @Nonnull
    public Map<String, String> process(@Nonnull String parameters) {

        final var parametersParser = new ConsoleParametersParser();
        final var parametersMap = parametersParser.parse(parameters);

        final var parametersFiller = new OptionalConsoleParametersFiller();
        parametersFiller.fill(parametersMap);

        final var parametersValidator = new ConsoleParametersValidator();
        parametersValidator.validate(parametersMap);

        return parametersMap;
    }
}
