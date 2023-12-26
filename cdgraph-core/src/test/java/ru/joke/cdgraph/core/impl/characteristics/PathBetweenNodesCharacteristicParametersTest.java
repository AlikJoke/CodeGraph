package ru.joke.cdgraph.core.impl.characteristics;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristicConfigurationException;
import ru.joke.cdgraph.core.impl.characteristics.paths.PathBetweenNodesCharacteristicParameters;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.joke.cdgraph.core.impl.util.TestUtil.SQL_MODULE;

public class PathBetweenNodesCharacteristicParametersTest {

    @Test
    public void testWhenTargetAndSourceAreSameThenException() {
        assertThrows(
                CodeGraphCharacteristicConfigurationException.class,
                () -> new PathBetweenNodesCharacteristicParameters(SQL_MODULE, SQL_MODULE)
        );
    }
}
