package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

/**
 * Specification for writing calculated characteristics above the graph:
 * specifies the output format and the sink where the data should be output.
 *
 * @author Alik
 *
 * @see Format
 * @see CodeGraphOutputSink
 */
public interface CodeGraphOutputSpecification {

    /**
     * Returns the output data format.
     * @return the output data format, can not be {@code null}.
     *
     * @see Format
     */
    @Nonnull
    Format format();

    /**
     * Returns a sink for data output.
     * @return a sink for data output, can not be {@code null}.
     *
     * @see CodeGraphOutputSink
     */
    @Nonnull
    CodeGraphOutputSink sink();

    /**
     * Enumeration of supported output formats.
     *
     * @author Alik
     */
    enum Format {

        /**
         * JSON format.
         */
        JSON {
            @Override
            @Nonnull
            public String convert(@Nonnull CodeGraphCharacteristicResult<?> characteristicResult) {
                return characteristicResult.toJson();
            }
        },

        /**
         * Simple text representation of the data (format is not standardized).
         */
        PLAIN_TEXT {
            @Override
            @Nonnull
            public String convert(@Nonnull CodeGraphCharacteristicResult<?> characteristicResult) {
                return characteristicResult.toPlainString();
            }
        };

        /**
         * Converts the result of a characteristic calculation into a string
         * in accordance with the specified output format.
         *
         * @param characteristicResult the result of a characteristic calculation, can not be {@code null}.
         * @return result of the calculation in the string format, can not be {@code null}.
         *
         * @see CodeGraphCharacteristicResult
         */
        @Nonnull
        public abstract String convert(@Nonnull CodeGraphCharacteristicResult<?> characteristicResult);
    }
}
