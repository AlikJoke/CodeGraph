package ru.joke.cdgraph.starter.console.params;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.client.CodeGraphOutputSpecification;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;
import ru.joke.cdgraph.starter.shared.CodeGraphDataSourceType;
import ru.joke.cdgraph.starter.shared.CodeGraphOutputSinkType;
import ru.joke.cdgraph.starter.shared.CodeGraphType;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.joke.cdgraph.starter.console.CodeGraphConsoleStarter.*;

public class ConsoleParametersProcessorTest {

    private static final String TEST_DATA_PATH = "/test.txt";
    private static final String CHARACTERISTIC = "abstractness:module-id=test;all.modules.stability";

    @Test
    public void testOptionalParametersFilling() {
        final var processor = new ConsoleParametersProcessor();

        final var args = new String[] {
                DATASOURCE_TYPE + "=" + CodeGraphDataSourceType.JAR.getAlias(),
                GRAPH_TYPE + "=" + CodeGraphType.MAVEN_MODULES.getAlias(),
                DATA_PATH + "=" + TEST_DATA_PATH,
                CHARACTERISTICS + "=" + CHARACTERISTIC
        };

        final Map<String, String> params = processor.process(arrayToString(args));
        assertNotNull(params, "Parameters must be not null");
        checkParameterExistence(params.get(DATASOURCE_TYPE), CodeGraphDataSourceType.JAR.getAlias());
        checkParameterExistence(params.get(GRAPH_TYPE), CodeGraphType.MAVEN_MODULES.getAlias());
        checkParameterExistence(params.get(DATA_PATH), TEST_DATA_PATH);
        checkParameterExistence(params.get(OUTPUT_FORMAT), CodeGraphOutputSpecification.Format.JSON.getAlias());
        checkParameterExistence(params.get(CHARACTERISTICS), CHARACTERISTIC);
        checkParameterExistence(params.get(SINK_TYPE), CodeGraphOutputSinkType.CONSOLE.getAlias());
        checkParameterExistence(params.get(SINK_TYPE), CodeGraphOutputSinkType.CONSOLE.getAlias());
    }

    @Test
    public void testRequiredParametersChecks() {
        final var processor = new ConsoleParametersProcessor();

        final var args = new String[] {
                DATASOURCE_TYPE + "=" + CodeGraphDataSourceType.JAR.getAlias(),
                DATA_PATH + "=" + TEST_DATA_PATH,
                CHARACTERISTICS + "=" + CHARACTERISTIC,
                SINK_TYPE + "=" + CodeGraphOutputSinkType.FILE.getAlias()
        };

        assertThrows(CodeGraphConfigurationException.class, () -> processor.process(arrayToString(args)), "Exception should be thrown when one of required parameter is not present");
    }

    private String arrayToString(final String[] args) {
        var sb = new StringBuilder();
        for (var arg : args) {
            sb.append(arg).append(" ");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    private void checkParameterExistence(final Object valueFromParams, final Object expectedParams) {
        assertEquals(expectedParams, valueFromParams, "Value from map must be equal");
    }
}
