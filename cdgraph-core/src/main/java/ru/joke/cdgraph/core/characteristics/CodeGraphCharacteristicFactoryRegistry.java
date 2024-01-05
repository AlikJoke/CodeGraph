package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

/**
 * Registry of module graph characteristics factories.
 * Allows to access the factory of the characteristic by a unique identifier,
 * as well as register the factory of the characteristic.
 *
 * @author Alik
 *
 * @see CodeGraphCharacteristic
 * @see CodeGraphCharacteristicParameters
 * @see CodeGraphCharacteristicFactoriesLoader
 */
public interface CodeGraphCharacteristicFactoryRegistry {

    /**
     * Registers the factory of the characteristic in the registry.
     *
     * @param characteristicFactory the factory of the characteristic, can not be {@code null}.
     * @param <T> type of the characteristic, can not be {@code null}.
     * @param <V> type of the result value of the characteristic, can not be {@code null}.
     * @param <K> type of the characteristic parameters, can not be {@code null}.
     *
     * @see CodeGraphCharacteristicFactoryRegistry#register(CodeGraphCharacteristicFactoriesLoader)
     */
    <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> void register(@Nonnull CodeGraphCharacteristicFactory<T, V, K> characteristicFactory);

    /**
     * Registers the factories returned by the specified loader in the registry.
     *
     * @param loader the loader of the factories, can not be {@code null}.
     * @see CodeGraphCharacteristicFactoriesLoader
     */
    void register(@Nonnull CodeGraphCharacteristicFactoriesLoader loader);

    /**
     * Returns a characteristic factory by identifier.
     *
     * @param id unique identifier of the factory, can not be {@code null}.
     * @return a factory of the characteristic, can not be {@code null}.
     * @param <T> type of the characteristic, can not be {@code null}.
     * @param <V> type of the result value of the characteristic, can not be {@code null}.
     * @param <K> type of the characteristic parameters, can not be {@code null}.
     * @throws CodeGraphCharacteristicFactoryNotFoundException if the factory of the characteristic not found
     */
    @Nonnull
    <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> find(@Nonnull String id);

    /**
     * Returns a characteristic factory by factory descriptor class.
     *
     * @param characteristicFactoryDescriptor type descriptor of factory, can not be {@code null}.
     * @return a factory of the characteristic, can not be {@code null}.
     * @param <A> type of the descriptor, can not be {@code null}.
     * @param <T> type of the characteristic, can not be {@code null}.
     * @param <V> type of the result value of the characteristic, can not be {@code null}.
     * @param <K> type of the characteristic parameters, can not be {@code null}.
     * @throws CodeGraphCharacteristicFactoryNotFoundException if the factory of the characteristic not found
     */
    @Nonnull
    <A extends Annotation, T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> find(@Nonnull Class<A> characteristicFactoryDescriptor);
}
