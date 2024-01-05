package ru.joke.cdgraph.starter.console.params;

import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;
import ru.joke.cdgraph.starter.shared.CodeGraphOutputSinkType;

import javax.annotation.Nonnull;
import java.util.Map;

import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.OUTPUT_FORMAT;
import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.SINK_TYPE;

final class OptionalConsoleParametersFiller {

    void fill(@Nonnull Map<String, String> parameters) {
        parameters.putIfAbsent(OUTPUT_FORMAT, CodeGraphOutputSpecification.Format.JSON.getAlias());
        parameters.putIfAbsent(SINK_TYPE, CodeGraphOutputSinkType.CONSOLE.getAlias());
    }
}
