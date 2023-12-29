package ru.joke.cdgraph.core.impl.characteristics.paths;

import org.junit.jupiter.api.Test;
import ru.joke.cdgraph.core.CodeGraphCharacteristicConfigurationException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.joke.cdgraph.core.impl.util.TestUtil.SQL_MODULE;

public class PathBetweenModulesCharacteristicParametersTest {

    @Test
    public void testWhenTargetAndSourceAreSameThenException() {
        assertThrows(
                CodeGraphCharacteristicConfigurationException.class,
                () -> new PathBetweenModulesCharacteristicParameters(SQL_MODULE, SQL_MODULE)
        );
    }
}
