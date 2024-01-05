package ru.joke.cdgraph.core.characteristics;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Factory of default values for parameters of constructors-factories of characteristics parameters.
 *
 * @param <T> type of the default value
 * @author Alik
 * @implSpec Implementation should be thread-safe
 * @see CodeGraphCharacteristicParameter
 */
@ThreadSafe
public interface CodeGraphCharacteristicParameterDefaultValueFactory<T> {

    /**
     * Creates the default value of the parameter.
     * @return the default value of the parameter, can be {@code null}.
     */
    T create();
}
