package ru.joke.cdgraph.core.characteristics.impl;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameterDefaultValueFactory;

/**
 * Default implementation of the {@link CodeGraphCharacteristicParameterDefaultValueFactory} for the {@code null} value.
 *
 * @author Alik
 * @see CodeGraphCharacteristicParameterDefaultValueFactory
 */
public final class NullCharacteristicParameterDefaultValueFactory implements CodeGraphCharacteristicParameterDefaultValueFactory<Object> {

    @Override
    public Object create() {
        return null;
    }
}
