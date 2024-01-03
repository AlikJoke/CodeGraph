package ru.joke.cdgraph.core.impl.characteristics;

import com.google.gson.Gson;
import ru.joke.cdgraph.core.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A simple implementation of the characteristic result wrapper.
 * @param <T> type of the result value
 *
 * @author Alik
 * @see CodeGraphCharacteristicResult
 */
public class SimpleCodeGraphCharacteristicResult<T> implements CodeGraphCharacteristicResult<T> {

    protected static final Gson gson = new Gson();

    private final String characteristicId;
    private final CodeGraphCharacteristicParameters parameters;
    private final T result;

    public SimpleCodeGraphCharacteristicResult(
            final String characteristicId,
            final T result) {
        this(characteristicId, null, result);
    }

    public SimpleCodeGraphCharacteristicResult(
            final String characteristicId,
            final CodeGraphCharacteristicParameters parameters,
            final T result) {
        this.result = result;
        this.characteristicId = characteristicId;
        this.parameters = parameters;
    }

    @Override
    @Nonnull
    public String toPlainString() {
        return characteristicId + ": { result: '" + result + "', parameters: '" + parameters + "'}";
    }

    @Override
    @Nonnull
    public String toJson() {
        return toJson(result);
    }

    @Override
    public T get() {
        return result;
    }

    protected String toJson(final Object result) {
        final Map<String, Object> data = Map.of(
                "characteristic", this.characteristicId,
                "parameters", parameters,
                "result", result
        );

        return gson.toJson(data);
    }
}
