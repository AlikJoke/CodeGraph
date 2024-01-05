package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;

/**
 * Representation of graph characteristic parameters.
 *
 * @author Alik
 */
public interface CodeGraphCharacteristicParameters {

    EmptyParameters emptyParameters = new EmptyParameters();

    /**
     * Creates empty characteristic parameters.
     * @return empty characteristic parameters, can not be {@code null}.
     */
    @Nonnull
    static CodeGraphCharacteristicParameters createEmpty() {
        return emptyParameters;
    }

    /**
     * Immutable empty characteristic parameters implementation.
     *
     * @author Alik
     */
    class EmptyParameters implements CodeGraphCharacteristicParameters {

        @Override
        public String toString() {
            return "no parameters";
        }
    }
}
