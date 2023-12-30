package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.List;

public interface CodeGraphRequest {

    @Nonnull
    CodeGraph codeGraph();

    @Nonnull
    List<CodeGraphCharacteristic<?>> requiredCharacteristics();
}
