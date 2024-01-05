package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A descriptor of the factory of the characteristic that computes all transitive chains (paths)
 * from the specified module node.
 *
 * @author Alik
 *
 * @see TransitiveChainsCharacteristic
 * @see TransitiveChainsCharacteristicFactoryDescriptor
 */
@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryDescriptor(
        characteristicId = "transitive.chains",
        characteristicParametersType = SingleModuleCharacteristicParameters.class
)
public @interface TransitiveChainsCharacteristicFactoryDescriptor {
}
