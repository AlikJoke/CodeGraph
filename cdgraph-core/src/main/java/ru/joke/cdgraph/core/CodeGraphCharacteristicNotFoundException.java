package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public final class CodeGraphCharacteristicNotFoundException extends RuntimeException {

    public CodeGraphCharacteristicNotFoundException(@Nonnull String id) {
        super("Characteristic not found by id: " + id);
    }
}
