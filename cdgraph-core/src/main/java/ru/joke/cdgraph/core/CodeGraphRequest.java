package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Representation of the query to a given graph to calculate characteristics.
 *
 * @author Alik
 *
 * @see CodeGraph
 * @see CodeGraphCharacteristic
 */
public interface CodeGraphRequest {

    /**
     * Returns the graph to which the characteristics calculation request is made.
     * @return the code graph, can not be {@code null}.
     *
     * @see CodeGraph
     */
    @Nonnull
    CodeGraph codeGraph();

    /**
     * Returns the list of characteristics to be computed for a given graph.
     * @return the list of characteristics, can not be {@code null}.
     *
     * @see CodeGraphCharacteristic
     */
    @Nonnull
    List<CodeGraphCharacteristic<?>> requiredCharacteristics();
}
