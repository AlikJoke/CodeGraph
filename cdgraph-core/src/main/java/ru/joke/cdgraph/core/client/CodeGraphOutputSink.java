package ru.joke.cdgraph.core.client;

import javax.annotation.Nonnull;

/**
 * Sink for outputting computed characteristics results.
 *
 * @author Alik
 */
public interface CodeGraphOutputSink extends AutoCloseable {

    /**
     * Writes a string of data to sink.
     * @param data a string of data, can not be {@code null}.
     */
    void write(@Nonnull String data);

    /**
     * Closes the sink.
     */
    @Override
    void close();
}
