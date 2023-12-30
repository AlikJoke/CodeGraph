package ru.joke.cdgraph.core.impl.characteristics.bridges;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("dependency.bridges")
public @interface DependencyBridgesCharacteristicFactoryHandle {
}
