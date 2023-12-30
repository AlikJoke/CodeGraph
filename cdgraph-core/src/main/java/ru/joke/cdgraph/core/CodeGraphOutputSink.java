package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphOutputSink extends AutoCloseable {

    void write(@Nonnull String data);

    @Override
    void close();
}
