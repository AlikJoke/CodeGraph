package ru.joke.cdgraph.maven.params;

import java.util.Map;

public final class MavenCodeGraphCharacteristicParameters {

    private double max;
    private double min;
    private Map<String, String> parameters;

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}