package ru.joke.cdgraph.core.std.util;

import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class TestUtil {

    public static final String TEST_MODULE_1 = "ru.joke.cdgraph.test_modules1";
    public static final String TEST_MODULE_2 = "ru.joke.cdgraph.test_modules2";
    public static final String TEST_MODULE_3 = "ru.joke.cdgraph.test_modules3";

    public static final String SQL_MODULE = "java.sql";
    public static final String BASE_MODULE = "java.base";

    public static final String TEST_MODULE_1_PATH = "modules/module-info1.class";
    public static final String TEST_MODULE_2_PATH = "modules/module-info2.class";
    public static final String TEST_MODULE_3_PATH = "modules/module-info3.class";

    public static CodeGraphDataSource createCodeGraphDatasource(@Nonnull final String... modulePath) {
        return new CodeGraphDataSource() {
            @Override
            @Nonnull
            public List<File> find(@Nonnull Predicate<String> filter) {
                try {
                    final List<File> result = new ArrayList<>(modulePath.length);
                    for (final String path : modulePath) {
                        final URL moduleDescriptorUrl = getClass().getClassLoader().getResource(path);
                        final File moduleDescriptorFile = new File(moduleDescriptorUrl.getFile());
                        final Path tempCopyFilePath = Files.createTempFile(UUID.randomUUID().toString(), null);
                        Files.copy(moduleDescriptorFile.toPath(), tempCopyFilePath, StandardCopyOption.REPLACE_EXISTING);

                        result.add(tempCopyFilePath.toFile());
                    }

                    return result;
                } catch (IOException ex) {
                    throw new CodeGraphDataSourceException(ex);
                }
            }
        };
    }

    private TestUtil() {
    }
}
