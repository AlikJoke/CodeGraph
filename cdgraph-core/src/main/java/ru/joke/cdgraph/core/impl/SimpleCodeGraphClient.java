package ru.joke.cdgraph.core.impl;

import ru.joke.cdgraph.core.CodeGraphClient;
import ru.joke.cdgraph.core.CodeGraphOutputSpecification;
import ru.joke.cdgraph.core.CodeGraphRequest;

import javax.annotation.Nonnull;

public final class SimpleCodeGraphClient implements CodeGraphClient {

    @Override
    public void execute(@Nonnull CodeGraphRequest request, @Nonnull CodeGraphOutputSpecification outputSpecification) {

        final var graph = request.codeGraph();
        final var sink = outputSpecification.sink();
        final var format = outputSpecification.format();
        try (sink) {
            request.requiredCharacteristics()
                    .stream()
                    .map(characteristic -> characteristic.compute(graph))
                    .map(format::convert)
                    .forEach(sink::write);
        }
    }
}
