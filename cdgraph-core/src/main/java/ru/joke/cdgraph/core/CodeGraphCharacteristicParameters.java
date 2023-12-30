package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphCharacteristicParameters {

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
