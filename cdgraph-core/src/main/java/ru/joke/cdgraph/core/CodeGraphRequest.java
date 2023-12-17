package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.util.List;

public interface CodeGraphRequest {

    @Nonnull
    CodeGraphDataSource dataSource();

    @Nonnull
    List<CodeGraphCharacteristic<?>> requiredCharacteristics();
}
