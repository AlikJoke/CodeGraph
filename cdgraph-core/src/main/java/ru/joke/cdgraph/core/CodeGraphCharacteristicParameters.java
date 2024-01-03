package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * Representation of graph characteristic parameters.
 *
 * @author Alik
 */
public interface CodeGraphCharacteristicParameters {

    /**
     * Creates empty characteristic parameters.
     * @return empty characteristic parameters, can not be {@code null}.
     */
    @Nonnull
    static CodeGraphCharacteristicParameters createEmpty() {
        return new CodeGraphCharacteristicParameters() {
            @Override
            public String toString() {
                return "no parameters";
            }
        };
    }
}
