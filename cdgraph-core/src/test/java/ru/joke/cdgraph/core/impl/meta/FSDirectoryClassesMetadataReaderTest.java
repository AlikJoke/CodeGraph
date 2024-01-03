package ru.joke.cdgraph.core.impl.meta;

import ru.joke.cdgraph.core.ClassMetadata;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static ru.joke.cdgraph.core.impl.util.TestUtil.TEST_JAR_3_PATH;
import static ru.joke.cdgraph.core.impl.util.TestUtil.unpackTestJarToDirectory;

public class FSDirectoryClassesMetadataReaderTest extends ClassesMetadataReaderTestBase {

    @Override
    protected Set<ClassMetadata> readMetadata() throws IOException, URISyntaxException {
        final Path targetDir = unpackTestJarToDirectory(TEST_JAR_3_PATH);
        final var paths = Files.find(targetDir, 2, ((path, basicFileAttributes) -> path.getFileName().toString().equals("classes") && basicFileAttributes.isDirectory()));
        try (paths) {
            return paths
                    .findAny()
                    .map(path -> new FSDirectoryClassesMetadataReader().read(path))
                    .orElseThrow();
        }
    }
}
