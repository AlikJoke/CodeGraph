package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Loader of the characteristic's factories for initializing registry factories.
 *
 * @author Alik
 * @see CodeGraphCharacteristicFactory
 * @see CodeGraphCharacteristicFactoryRegistry
 */
public interface CodeGraphCharacteristicFactoriesLoader {

    /**
     * Loads the characteristic's factories.
     * @return loaded factories, can not be {@code null}.
     */
    @Nonnull
    Set<CodeGraphCharacteristicFactory<?, ?, ?>> load();
}
