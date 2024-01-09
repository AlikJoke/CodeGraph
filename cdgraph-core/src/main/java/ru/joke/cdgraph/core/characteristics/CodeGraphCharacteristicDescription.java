package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * Description of the characteristic.
 *
 * @author Alik
 * @see CodeGraphCharacteristicFactoryDescriptor
 * @see CodeGraphCharacteristicParameter
 */
public interface CodeGraphCharacteristicDescription {

    /**
     * Returns the descriptor of the characteristic's factory.
     * @return descriptor, can not be {@code null}.
     * @see CodeGraphCharacteristicFactoryDescriptor
     */
    @Nonnull
    CodeGraphCharacteristicFactoryDescriptor factoryDescriptor();

    /**
     * Returns the parameter's descriptors of the characteristic.
     * @return parameter's descriptors, can not be {@code null}.
     * @see CodeGraphCharacteristicParameter
     */
    @Nonnull
    Set<CodeGraphCharacteristicParameter> parameters();
}
