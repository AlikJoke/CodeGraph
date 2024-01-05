package ru.joke.cdgraph.core.characteristics.impl.counters;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes module dependencies count.
 *
 * @author Alik
 *
 * @see DependenciesCountCharacteristic
 * @see DependenciesCountCharacteristicFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(
        characteristicId = "dependencies.count",
        characteristicParametersType = SingleModuleCharacteristicParameters.class
)
public @interface DependenciesCountCharacteristicFactoryDescriptor {
}
