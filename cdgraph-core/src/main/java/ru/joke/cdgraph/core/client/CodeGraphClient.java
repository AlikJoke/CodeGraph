package ru.joke.cdgraph.core.client;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A client for executing queries against a code modules graph. Contains a query object
 * for the graph and a specification for outputting the results of computation the
 * characteristics of the graph.
 *
 * @author Alik
 *
 * @see CodeGraphRequest
 * @see CodeGraphOutputSpecification
 */
public interface CodeGraphClient {

    /**
     * Executes the query to the code modules graph.
     *
     * @param request request query to the graph, can not be {@code null}.
     * @param outputSpecification specification of data output of characteristics calculation results, can not be {@code null}.
     * @return results of the characteristic's computing, can not be {@code null}.
     * @see CodeGraphCharacteristicResult
     */
    @Nonnull
    List<CodeGraphCharacteristicResult<?>> execute(@Nonnull CodeGraphRequest request, @Nonnull CodeGraphOutputSpecification outputSpecification);
}
