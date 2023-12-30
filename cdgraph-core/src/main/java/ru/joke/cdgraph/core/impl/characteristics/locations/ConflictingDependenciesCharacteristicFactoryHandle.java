package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("conflicting.dependencies")
public @interface ConflictingDependenciesCharacteristicFactoryHandle {
}
