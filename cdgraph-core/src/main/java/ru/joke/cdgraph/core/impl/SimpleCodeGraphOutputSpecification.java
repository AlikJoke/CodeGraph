package ru.joke.cdgraph.core.impl;

import ru.joke.cdgraph.core.CodeGraphOutputSink;
import ru.joke.cdgraph.core.CodeGraphOutputSpecification;

import javax.annotation.Nonnull;

public record SimpleCodeGraphOutputSpecification(
        @Nonnull Format format,
        @Nonnull CodeGraphOutputSink sink) implements CodeGraphOutputSpecification {
}
