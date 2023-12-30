package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("all.paths.between.modules")
public @interface AllPathsBetweenModulesCharacteristicFactoryHandle {
}
