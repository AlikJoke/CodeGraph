package ru.joke.cdgraph.core.impl.characteristics;

import com.google.gson.Gson;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;

public class SimpleCodeGraphCharacteristicResult<T> implements CodeGraphCharacteristicResult<T> {

    protected static final Gson gson = new Gson();

    private final T result;

    public SimpleCodeGraphCharacteristicResult(final T result) {
        this.result = result;
    }

    @Override
    public String toPlainString() {
        return result.toString();
    }

    @Override
    public String toJson() {
        return gson.toJson(result);
    }

    @Override
    public T get() {
        return result;
    }
}
