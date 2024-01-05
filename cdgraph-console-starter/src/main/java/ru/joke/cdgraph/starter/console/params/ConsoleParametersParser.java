package ru.joke.cdgraph.starter.console.params;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

final class ConsoleParametersParser {

    private static final String DELIMITER = "=";

    @Nonnull
    Map<String, String> parse(@Nonnull String parameters) {
        return parse(parameters.split(" "));
    }

    @Nonnull
    private Map<String, String> parse(@Nonnull String[] args) {
        return Arrays.stream(args)
                        .map(arg -> arg.split(DELIMITER))
                        .collect(Collectors.toMap(argParts -> argParts[0], this::getArgValue));
    }

    private String getArgValue(final String[] argParts) {
        return switch (argParts.length) {
            case 1 -> "";
            case 2 -> argParts[1];
            default -> arrayValuePartsToString(argParts);
        };
    }

    private String arrayValuePartsToString(final String[] argParts) {
        final var sb = new StringBuilder();
        for (int i = 1; i < argParts.length; i++) {
            sb.append(argParts[i]).append(DELIMITER);
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
