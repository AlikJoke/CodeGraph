package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphClient {

    void execute(@Nonnull CodeGraphRequest request, @Nonnull CodeGraphOutputSpecification outputSpecification);
}
