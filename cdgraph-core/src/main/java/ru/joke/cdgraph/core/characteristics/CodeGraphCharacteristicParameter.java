package ru.joke.cdgraph.core.characteristics;

import ru.joke.cdgraph.core.characteristics.impl.NoOpCharacteristicParameterValueTransformer;
import ru.joke.cdgraph.core.characteristics.impl.NullCharacteristicParameterDefaultValueFactory;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Parameter descriptor of the constructor-factory for characteristic parameters.
 *
 * @author Alik
 * @see CodeGraphCharacteristicParametersCreator
 * @see CodeGraphCharacteristicParameterValueTransformer
 * @see CodeGraphCharacteristicParameterDefaultValueFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CodeGraphCharacteristicParameter {

    /**
     * Returns the parameter identifier.
     * @return the parameter identifier, can not be {@code null}.
     */
    @Nonnull
    String id();

    /**
     * Returns the parameter description.
     * @return the parameter description, can not be {@code null}.
     */
    @Nonnull
    String description() default "";

    /**
     * Returns whether the parameter value is required.
     * @return whether the parameter value is required, {@code true} if required.
     */
    boolean required() default false;

    /**
     * Returns the default parameter value factory type.
     * @return the default parameter value factory type, can not be {@code null}.
     * @see CodeGraphCharacteristicParameterDefaultValueFactory
     */
    @Nonnull
    Class<? extends CodeGraphCharacteristicParameterDefaultValueFactory<?>> defaultValueFactory() default NullCharacteristicParameterDefaultValueFactory.class;

    /**
     * Returns the transformer of the parameter value from {@link String}.
     * @return the transformer type, can not be {@code null}.
     * @see CodeGraphCharacteristicParameterValueTransformer
     */
    @Nonnull
    Class<? extends CodeGraphCharacteristicParameterValueTransformer<?>> valueTransformer() default NoOpCharacteristicParameterValueTransformer.class;
}
