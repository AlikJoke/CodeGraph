package ru.joke.cdgraph.starter.console;

import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;
import ru.joke.cdgraph.core.client.CodeGraphOutputSink;
import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;
import ru.joke.cdgraph.core.client.impl.CodeGraphOutputFileSink;
import ru.joke.cdgraph.core.client.impl.CodeGraphOutputStreamSink;
import ru.joke.cdgraph.starter.shared.CodeGraphOutputSinkType;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.util.Map;

import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.*;

final class ConsoleCodeGraphOutputSpecification implements CodeGraphOutputSpecification {

    private final Map<String, String> parameters;

    ConsoleCodeGraphOutputSpecification(@Nonnull Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public Format format() {
        return Format.from(this.parameters.get(OUTPUT_FORMAT));
    }

    @Nonnull
    @Override
    public CodeGraphOutputSink sink() {
        final var sinkType = CodeGraphOutputSinkType.from(this.parameters.get(SINK_TYPE));
        return switch (sinkType) {
            case CONSOLE -> new CodeGraphOutputStreamSink();
            case FILE -> createFileSink();
        };
    }

    private CodeGraphOutputFileSink createFileSink() {
        try {
            final String filePath = this.parameters.get(OUTPUT_FILE_PATH);
            return new CodeGraphOutputFileSink(filePath);
        } catch (FileNotFoundException ex) {
            throw new CodeGraphConfigurationException(ex);
        }
    }
}
