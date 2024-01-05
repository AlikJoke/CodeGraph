package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Factory of the characteristic parameters. Creates parameters object from the different
 * parameters' representations and factory of the characteristic.
 *
 * @author Alik
 * @see CodeGraphCharacteristicFactory
 * @see CodeGraphCharacteristicParameters
 */
public interface CodeGraphCharacteristicParametersFactory {

    /**
     * Creates characteristic parameters by the factory of the characteristic and parameters
     * from the {@link Map}.
     *
     * @param factory factory of the characteristic, can not be {@code null}.
     * @param parametersMap parameters map, can not be {@code null}.
     * @return created parameters, can not be {@code null}.
     * @param <K> type of the parameters
     */
    @Nonnull
    <K extends CodeGraphCharacteristicParameters> K createFor(
            @Nonnull CodeGraphCharacteristicFactory<?, ?, K> factory,
            @Nonnull Map<String, String> parametersMap
    );
}
