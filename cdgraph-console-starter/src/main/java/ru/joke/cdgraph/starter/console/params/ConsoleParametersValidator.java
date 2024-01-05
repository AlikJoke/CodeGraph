package ru.joke.cdgraph.starter.console.params;

import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;
import ru.joke.cdgraph.starter.shared.CodeGraphOutputSinkType;

import javax.annotation.Nonnull;
import java.util.Map;

import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.*;

final class ConsoleParametersValidator {

    void validate(@Nonnull Map<String, String> parameters) {
        checkParameterExistence(parameters, DATA_PATH);
        checkParameterExistence(parameters, DATASOURCE_TYPE);
        checkParameterExistence(parameters, GRAPH_TYPE);
        checkParameterExistence(parameters, CHARACTERISTICS);

        final var sinkType = parameters.get(SINK_TYPE);
        if (CodeGraphOutputSinkType.FILE.getAlias().equalsIgnoreCase(sinkType)) {
            checkParameterExistence(parameters, OUTPUT_FILE_PATH);
        }
    }

    private void checkParameterExistence(final Map<String, String> parameters, final String parameter) {
        final var parameterValue = parameters.get(parameter);
        if (parameterValue == null || parameterValue.isBlank()) {
            throw new CodeGraphConfigurationException("Parameter '" + parameter + "' does not specified");
        }
    }


}
