package ru.joke.cdgraph.core.client.impl;

import ru.joke.cdgraph.core.client.CodeGraphOutputSink;
import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;

import javax.annotation.Nonnull;

public record SimpleCodeGraphOutputSpecification(
        @Nonnull Format format,
        @Nonnull CodeGraphOutputSink sink) implements CodeGraphOutputSpecification {
}
