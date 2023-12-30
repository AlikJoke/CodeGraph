package ru.joke.cdgraph.core.impl.sinks;

import ru.joke.cdgraph.core.CodeGraphOutputSink;

import javax.annotation.Nonnull;
import java.io.PrintStream;

public final class CodeGraphOutputStreamSink implements CodeGraphOutputSink {

    private final PrintStream outputStream;

    public CodeGraphOutputStreamSink(@Nonnull PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public CodeGraphOutputStreamSink() {
        this.outputStream = System.out;
    }

    @Override
    public void write(@Nonnull String data) {
        this.outputStream.println(data);
    }

    @Override
    public void close() {

    }
}
