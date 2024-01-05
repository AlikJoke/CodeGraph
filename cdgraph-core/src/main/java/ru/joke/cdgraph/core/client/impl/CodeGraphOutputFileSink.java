package ru.joke.cdgraph.core.client.impl;

import ru.joke.cdgraph.core.client.CodeGraphOutputSink;
import ru.joke.cdgraph.core.client.CodeGraphSinkException;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * File sink for outputting the results of computing characteristics.
 *
 * @author Alik
 * @see CodeGraphOutputSink
 */
public final class CodeGraphOutputFileSink implements CodeGraphOutputSink {

    private final OutputStream stream;

    public CodeGraphOutputFileSink(@Nonnull File file) throws FileNotFoundException {
        if (file.isDirectory()) {
            throw new CodeGraphSinkException("Output file must not be directory");
        } else if (!file.canWrite()) {
            throw new CodeGraphSinkException("The file should be available for writing");
        }

        this.stream = new FileOutputStream(file);
    }

    public CodeGraphOutputFileSink(@Nonnull String path) throws FileNotFoundException {
        this(new File(path));
    }

    public CodeGraphOutputFileSink(@Nonnull Path path) throws FileNotFoundException {
        this(path.toFile());
    }

    @Override
    public void write(@Nonnull String data) {
        try {
            final String dataToWrite = data + System.lineSeparator();
            this.stream.write(dataToWrite.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new CodeGraphSinkException(e);
        }
    }

    @Override
    public void close() {
        try {
            this.stream.flush();
            this.stream.close();
        } catch (IOException e) {
            throw new CodeGraphSinkException(e);
        }
    }
}
