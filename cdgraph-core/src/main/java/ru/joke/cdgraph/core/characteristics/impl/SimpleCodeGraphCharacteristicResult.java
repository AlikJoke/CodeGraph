package ru.joke.cdgraph.core.characteristics.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * A simple abstract implementation of the characteristic result wrapper.
 * @param <T> type of the result value
 *
 * @author Alik
 * @see CodeGraphCharacteristicResult
 */
public abstract class SimpleCodeGraphCharacteristicResult<T> implements CodeGraphCharacteristicResult<T> {

    protected static final Gson gson =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                    .create();

    private final String characteristicId;
    private final CodeGraphCharacteristicParameters parameters;
    private final T result;

    public SimpleCodeGraphCharacteristicResult(
            final String characteristicId,
            final T result) {
        this(characteristicId, CodeGraphCharacteristicParameters.createEmpty(), result);
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

    @Override
    @Nonnull
    public String characteristicId() {
        return characteristicId;
    }

    protected String toJson(final Object result) {
        final Map<String, Object> data = Map.of(
                "characteristicId", this.characteristicId,
                "parameters", parameters,
                "result", result
        );

        return gson.toJson(data);
    }
}
