package ru.joke.cdgraph.maven.params;

import java.util.Map;

public final class MavenCodeGraphCharacteristicParameters {

    private String boundExpression;
    private Map<String, String> parameters;

    public String getBoundExpression() {
        return boundExpression;
    }

    public void setBoundExpression(String boundExpression) {
        this.boundExpression = boundExpression;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}