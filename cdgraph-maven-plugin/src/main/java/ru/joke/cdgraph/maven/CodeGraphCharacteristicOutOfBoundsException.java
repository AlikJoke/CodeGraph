package ru.joke.cdgraph.maven;

final class CodeGraphCharacteristicOutOfBoundsException extends RuntimeException {

    CodeGraphCharacteristicOutOfBoundsException(final String characteristicId, final String actualValue) {
        super("Value of the characteristic '%s' out of configured bounds: %s".formatted(characteristicId, actualValue));
    }
}
