package ru.joke.cdgraph.core.characteristics;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * Base descriptor annotation of the factory.<br>
 * Specific factory descriptors must be marked with this base annotation.
 *
 * @author Alik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Inherited
public @interface CodeGraphCharacteristicFactoryDescriptor {

    /**
     * Returns the unique identifier of the characteristic.
     * @return the unique identifier of the characteristic, can not be {@code null}.
     */
    @Nonnull
    String characteristicId();

    /**
     * Returns the parameters type of the characteristic.
     * @return the parameters type of the characteristic, can not be {@code null}.
     * @see CodeGraphCharacteristicParameters
     */
    @Nonnull
    Class<? extends CodeGraphCharacteristicParameters> characteristicParametersType() default CodeGraphCharacteristicParameters.EmptyParameters.class;
}
