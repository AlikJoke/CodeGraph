package ru.joke.cdgraph.core;

import javax.annotation.Nonnull;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Inherited
public @interface CodeGraphCharacteristicFactoryHandle {

    @Nonnull
    String value();
}
