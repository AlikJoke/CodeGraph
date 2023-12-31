package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSourceException;
import ru.joke.cdgraph.core.CodeGraphDataSourceFactory;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class CodeGraphWarDataSource extends CodeGraphJarDataSource {

    private static final String WEB_INF_DIR = "WEB-INF/";
    private static final String WAR_DATA_DIR = WEB_INF_DIR + "classes/";
    private static final String WAR_LIB_DIR = WEB_INF_DIR + "lib/";
    private static final String JAR_EXTENSION = ".jar";

    private final CodeGraphDataSourceFactory jarDataSourceFactory;

    public CodeGraphWarDataSource(
            @Nonnull Path dataSourcePath,
            @Nonnull ClassesMetadataReader warMetadataReader,
            @Nonnull CodeGraphDataSourceFactory jarDataSourceFactory) {
        super(dataSourcePath, warMetadataReader);
        this.jarDataSourceFactory = jarDataSourceFactory;
    }

    @Nonnull
    @Override
    public List<Configuration> find(@Nonnull Predicate<String> descriptorFileFilter) {

        final var warModuleConfigurations = super.find(descriptorFileFilter);
        try (final JarFile jar = new JarFile(this.dataSourcePath.toFile())) {
            final var nestedJarsConfigurations =
                    jar.stream()
                        .filter(entry -> entry.getName().startsWith(WAR_LIB_DIR) && entry.getName().endsWith(JAR_EXTENSION))
                        .map(entry -> convertEntryToFile(jar, entry))
                        .map(File::toPath)
                        .map(this.jarDataSourceFactory::create)
                        .map(ds -> ds.find(descriptorFileFilter))
                        .flatMap(List::stream)
                        .toList();

            final List<Configuration> result = new ArrayList<>(warModuleConfigurations.size() + nestedJarsConfigurations.size());
            result.addAll(warModuleConfigurations);
            result.addAll(nestedJarsConfigurations);

            return result;
        } catch (IOException e) {
            throw new CodeGraphDataSourceException(e);
        }
    }

    @Nonnull
    @Override
    protected String getEntryName(@Nonnull JarEntry entry) {
        final var entryName = entry.getName();
        return entryName.startsWith(WAR_DATA_DIR)
                ? entryName.substring(WAR_DATA_DIR.length())
                : entryName;
    }
}
