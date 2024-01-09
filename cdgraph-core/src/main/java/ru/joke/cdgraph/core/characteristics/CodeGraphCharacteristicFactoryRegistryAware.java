package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;

/**
 * An interface for marking characteristic's factories that require access
 * to the factory of the characteristic registry.
 *
 * @author Alik
 *
 * @see CodeGraphCharacteristicService
 */
public interface CodeGraphCharacteristicFactoryRegistryAware {

    /**
     * Sets the characteristic's factories registry for a given characteristic factory.
     * @param registry registry of the characteristic's factories, can not be {@code null}.
     */
    void setRegistry(@Nonnull CodeGraphCharacteristicService registry);
}
