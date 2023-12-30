package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("all.modules.abstractness")
public @interface AllModulesAbstractnessCharacteristicFactoryHandle {
}
