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

public class CodeGraphFatJarDataSource extends CodeGraphJarDataSource {

    protected static final String JAR_EXTENSION = ".jar";
    private static final String JAR_DATA_DIR = "classes";
    private static final String JAR_DEPENDENCIES_DIR = "lib";

    private final CodeGraphDataSourceFactory jarDataSourceFactory;

    public CodeGraphFatJarDataSource(
            @Nonnull Path dataSourcePath,
            @Nonnull ClassesMetadataReader jarMetadataReader,
            @Nonnull CodeGraphDataSourceFactory jarDataSourceFactory) {
        super(dataSourcePath, jarMetadataReader);
        this.jarDataSourceFactory = jarDataSourceFactory;
    }

    @Nonnull
    @Override
    public List<Configuration> find(@Nonnull Predicate<String> descriptorFileFilter) {

        final var jarModuleConfigurations = super.find(descriptorFileFilter);
        try (final JarFile jar = new JarFile(this.dataSourcePath.toFile())) {
            final var nestedJarsConfigurations =
                    jar.stream()
                            .filter(this::isNestedJarFile)
                            .map(entry -> convertEntryToFile(jar, entry))
                            .map(File::toPath)
                            .map(this.jarDataSourceFactory::create)
                            .map(ds -> ds.find(descriptorFileFilter))
                            .flatMap(List::stream)
                            .toList();

            final List<Configuration> result = new ArrayList<>(jarModuleConfigurations.size() + nestedJarsConfigurations.size());
            result.addAll(jarModuleConfigurations);
            result.addAll(nestedJarsConfigurations);

            return result;
        } catch (IOException e) {
            throw new CodeGraphDataSourceException(e);
        }
    }

    @Override
    public String toString() {
        return "CodeGraphFatJarDataSource{" + "dataSourcePath=" + dataSourcePath + '}';
    }

    @Nonnull
    @Override
    protected String getEntryName(@Nonnull JarEntry entry) {
        final var entryName = entry.getName();
        final var nameEntries = entryName.split("/");
        if (nameEntries.length < 2) {
            return entryName;
        }

        return nameEntries[1].equals(JAR_DATA_DIR)
                ? nameEntries[nameEntries.length - 1]
                : entryName;
    }

    protected boolean isNestedJarFile(@Nonnull JarEntry entry) {
        return entry.getName().contains("/" + JAR_DEPENDENCIES_DIR + "/")
                && entry.getName().endsWith(JAR_EXTENSION);
    }
}

