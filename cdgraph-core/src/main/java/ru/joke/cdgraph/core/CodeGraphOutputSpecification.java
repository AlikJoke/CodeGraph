package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphOutputSpecification {

    @Nonnull
    Format format();

    @Nonnull
    CodeGraphOutputSink sink();

    enum Format {

        JSON {
            @Override
            public String convert(@Nonnull CodeGraphCharacteristicResult<?> characteristicResult) {
                return characteristicResult.toJson();
            }
        },

        PLAIN_TEXT {
            @Override
            public String convert(@Nonnull CodeGraphCharacteristicResult<?> characteristicResult) {
                return characteristicResult.toPlainString();
            }
        };

        public abstract String convert(@Nonnull CodeGraphCharacteristicResult<?> characteristicResult);
    }
}
