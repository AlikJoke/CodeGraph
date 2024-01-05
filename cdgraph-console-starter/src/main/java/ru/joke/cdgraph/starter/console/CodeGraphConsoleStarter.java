package ru.joke.cdgraph.starter.console;

import ru.joke.cdgraph.core.client.impl.SimpleCodeGraphClient;
import ru.joke.cdgraph.starter.console.params.ConsoleParametersProcessor;

import java.util.Scanner;

/**
 * Console CodeGraph client starter. Requests for data calculation and output are made
 * using command line arguments.<br>
 * Format description:
 * <ul>
 * <li>Separator between different command line arguments: space</li>
 * <li>Separator between value and parameter identifier: {@literal =}</li>
 * <li>Separator between different characteristics: {@literal ;}</li>
 * <li>Separator between the characteristic identifier and its parameters: {@literal :}</li>
 * <li>Separator between different parameters of one characteristic: {@literal =}</li>
 * </ul>
 * Example: {@code ds-type=war graph-type=maven-modules characteristics=c1:prm11=val11,prm12=val12;c2:prm21=val21;c3;}
 *
 * @author Alik
 */
public final class CodeGraphConsoleStarter {

    public static final String DATA_PATH = "ds-path";
    public static final String GRAPH_TYPE = "graph-type";
    public static final String DATASOURCE_TYPE = "ds-type";
    public static final String NESTED_DATASOURCE_TYPE = "nested-ds-type";
    public static final String OUTPUT_FORMAT = "output-format";
    public static final String SINK_TYPE = "sink";
    public static final String OUTPUT_FILE_PATH = "output-file";
    public static final String CHARACTERISTICS = "characteristics";
    public static final String CACHE_GRAPH = "enable-graph-caching";

    public static final String MAVEN_REPO_BASE_URL = "maven-repository-url";
    public static final String MAVEN_REPO_USER = "maven-repository-user";
    public static final String MAVEN_REPO_PWD = "maven-repository-password";

    private static final String STOP_COMMAND = "stop";

    private static final String AWAIT_NEXT_COMMAND_INFO = "Ready to next request...";
    private static final String STARTED_INFO = "Started, ready to requests";
    private static final String STOPPED_INFO = "Successfully stopped";

    public static void main(String... args) {
        try (final var scanner = new Scanner(System.in)) {
            run(scanner);
        }
    }

    private static void run(final Scanner scanner) {
        println(STARTED_INFO);

        while (true) {
            final String command = scanner.hasNextLine() ? scanner.nextLine() : null;
            if (STOP_COMMAND.equalsIgnoreCase(command)) {
                break;
            }

            if (command != null) {
                executeRequestNoEx(command);
            }
        }

        println(STOPPED_INFO);
    }

    private static void executeRequestNoEx(final String parameters) {
        try {
            executeRequest(parameters);
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.err);
        }

        println(AWAIT_NEXT_COMMAND_INFO);
    }

    private static void println(final String line) {
        System.out.println(line);
    }

    private static void executeRequest(final String parameters) {
        final var parametersProcessor = new ConsoleParametersProcessor();
        final var parametersMap = parametersProcessor.process(parameters);

        final var codeGraphRequest = new ConsoleCodeGraphRequest(parametersMap);
        final var outputSpecification = new ConsoleCodeGraphOutputSpecification(parametersMap);

        final var client = new SimpleCodeGraphClient();
        client.execute(codeGraphRequest, outputSpecification);
    }
}
