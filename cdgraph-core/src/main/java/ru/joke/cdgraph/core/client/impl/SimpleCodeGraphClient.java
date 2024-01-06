package ru.joke.cdgraph.core.client.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.client.CodeGraphClient;
import ru.joke.cdgraph.core.client.CodeGraphOutputSink;
import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;
import ru.joke.cdgraph.core.client.CodeGraphRequest;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public final class SimpleCodeGraphClient implements CodeGraphClient {

    @Override
    @Nonnull
    public List<CodeGraphCharacteristicResult<?>> execute(@
            Nonnull CodeGraphRequest request,
            @Nonnull CodeGraphOutputSpecification outputSpecification) {

        final var graph = request.codeGraph();
        final var sink = outputSpecification.sink();
        final var format = outputSpecification.format();
        try (sink) {
            return request.requiredCharacteristics()
                            .stream()
                            .map(characteristic -> characteristic.compute(graph))
                            .peek(result -> writeToSink(sink, format, result))
                            .collect(Collectors.toList());
        }
    }

    private void writeToSink(
            final CodeGraphOutputSink sink,
            final CodeGraphOutputSpecification.Format format,
            final CodeGraphCharacteristicResult<?> result) {
        sink.write(format.convert(result));
    }
}
