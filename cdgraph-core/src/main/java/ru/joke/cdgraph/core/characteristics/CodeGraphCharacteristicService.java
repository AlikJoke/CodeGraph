package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * CodeGraph characteristic's service. Allows to access the factory and the description
 * of the characteristic by a unique identifier, as well as register the factory of the characteristic.
 *
 * @author Alik
 *
 * @see CodeGraphCharacteristic
 * @see CodeGraphCharacteristicDescription
 * @see CodeGraphCharacteristicParameters
 * @see CodeGraphCharacteristicFactoriesLoader
 */
public interface CodeGraphCharacteristicService {

    /**
     * Registers the factory of the characteristic in the registry.
     *
     * @param characteristicFactory the factory of the characteristic, can not be {@code null}.
     * @param <T> type of the characteristic, can not be {@code null}.
     * @param <V> type of the result value of the characteristic, can not be {@code null}.
     * @param <K> type of the characteristic parameters, can not be {@code null}.
     *
     * @see CodeGraphCharacteristicService#registerFactories(CodeGraphCharacteristicFactoriesLoader)
     */
    <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> void registerFactory(@Nonnull CodeGraphCharacteristicFactory<T, V, K> characteristicFactory);

    /**
     * Registers the factories returned by the specified loader in the registry.
     *
     * @param loader the loader of the factories, can not be {@code null}.
     * @see CodeGraphCharacteristicFactoriesLoader
     */
    void registerFactories(@Nonnull CodeGraphCharacteristicFactoriesLoader loader);

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
    <T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> findFactory(@Nonnull String id);

    /**
     * Returns a characteristic's factory by factory descriptor class.
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
    <A extends Annotation, T extends CodeGraphCharacteristic<V>, V, K extends CodeGraphCharacteristicParameters> CodeGraphCharacteristicFactory<T, V, K> findFactory(@Nonnull Class<A> characteristicFactoryDescriptor);

    /**
     * Returns the description of the characteristic by id.
     * @param characteristicId id of the characteristic, can not be {@code null}.
     * @return description of the characteristic, can not be {@code null}.
     * @throws CodeGraphCharacteristicDescriptionNotFoundException if the description of the characteristic not found
     * @see CodeGraphCharacteristicDescription
     */
    @Nonnull
    CodeGraphCharacteristicDescription findDescription(@Nonnull String characteristicId);

    /**
     * Returns the characteristic's descriptions.
     * @return the characteristic's descriptions, can not be {@code null}.
     */
    @Nonnull
    Set<CodeGraphCharacteristicDescription> findDescriptions();
}
