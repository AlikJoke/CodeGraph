package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public final class CodeGraphCharacteristicDefinitionNotFoundException extends RuntimeException {

    public CodeGraphCharacteristicDefinitionNotFoundException(@Nonnull String id) {
        super("Characteristic definition not found by id: " + id);
    }
}
