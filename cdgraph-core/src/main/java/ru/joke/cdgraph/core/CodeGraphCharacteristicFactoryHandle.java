package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

/**
 * Base handle annotation of the factory.<br>
 * Specific factory handles must be marked with this base annotation.
 *
 * @author Alik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Inherited
public @interface CodeGraphCharacteristicFactoryHandle {

    /**
     * Returns the unique identifier of the factory handle.
     * @return the unique identifier of the factory handle, can not be {@code null}.
     */
    @Nonnull
    String value();
}
