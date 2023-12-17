package ru.joke.cdgraph.core;

public interface CodeGraphCharacteristicResult<T> {

    String toPlainString();

    String toJson();

    T get();
}
