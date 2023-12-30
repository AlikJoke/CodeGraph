package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("shortest.path.between.modules")
public @interface ShortestPathBetweenModulesCharacteristicFactoryHandle {
}
