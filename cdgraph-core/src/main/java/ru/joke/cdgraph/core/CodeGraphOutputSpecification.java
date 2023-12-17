package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphOutputSpecification {

    @Nonnull
    Format format();

    @Nonnull
    CodeGraphOutputSink sink();

    enum Format {

        JSON,

        PLAIN_TEXT
    }
}
