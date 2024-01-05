package ru.joke.cdgraph.core.meta.impl;

import ru.joke.cdgraph.core.meta.ClassMetadata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.jar.JarFile;

import static ru.joke.cdgraph.core.test.util.TestUtil.TEST_JAR_3_PATH;
import static ru.joke.cdgraph.core.test.util.TestUtil.getJarFile;

public class JarClassesMetadataReaderTest extends ClassesMetadataReaderTestBase {

    @Override
    protected Set<ClassMetadata> readMetadata() throws IOException, URISyntaxException {
        try (final JarFile jar = new JarFile(getJarFile(TEST_JAR_3_PATH), false)) {
            return new JarClassesMetadataReader().read(jar);
        }
    }
}
