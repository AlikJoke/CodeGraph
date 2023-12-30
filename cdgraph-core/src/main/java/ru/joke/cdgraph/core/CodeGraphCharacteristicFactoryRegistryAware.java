package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;

public interface CodeGraphCharacteristicFactoryRegistryAware {

    void setRegistry(@Nonnull CodeGraphCharacteristicFactoryRegistry registry);
}
