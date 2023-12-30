package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

public interface CodeGraphCharacteristicFactoryRegistry {

    <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> void register(@Nonnull CodeGraphCharacteristicFactory<T, V, K> characteristicFactory);

    void register(@Nonnull CodeGraphCharacteristicFactoriesLoader loader);

    @Nonnull
    <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> find(@Nonnull String id);

    @Nonnull
    <A extends Annotation, T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> find(@Nonnull Class<A> characteristicFactoryHandle);
}
