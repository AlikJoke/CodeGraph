package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.CodeGraphCharacteristicFactoryHandle;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@CodeGraphCharacteristicFactoryHandle("module.clustering")
public @interface ModuleClusteringCharacteristicFactoryHandle {
}
