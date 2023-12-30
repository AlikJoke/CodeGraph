package ru.joke.cdgraph.core.impl.sinks;

import ru.joke.cdgraph.core.CodeGraphOutputSink;

import javax.annotation.Nonnull;
import java.util.Collection;

public final class CodeGraphOutputInMemorySink implements CodeGraphOutputSink {

    private final Collection<String> data;

    public CodeGraphOutputInMemorySink(@Nonnull Collection<String> data) {
        this.data = data;
    }

    @Override
    public void write(@Nonnull String data) {
        this.data.add(data);
    }

    @Override
    public void close() {
    }
}
