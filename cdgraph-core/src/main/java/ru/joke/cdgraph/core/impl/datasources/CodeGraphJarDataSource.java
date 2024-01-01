package ru.joke.cdgraph.core.impl.datasources;

import ru.joke.cdgraph.core.ClassesMetadataReader;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphDataSourceException;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CodeGraphJarDataSource implements CodeGraphDataSource {

    protected final Path dataSourcePath;
    protected final ClassesMetadataReader metadataReader;

    public CodeGraphJarDataSource(
            @Nonnull Path dataSourcePath,
            @Nonnull ClassesMetadataReader metadataReader) {
        this.dataSourcePath = dataSourcePath;
        this.metadataReader = metadataReader;
    }

    @Nonnull
    @Override
    public String id() {
        return dataSourcePath.getFileName().toString();
    }

    @Nonnull
    @Override
    public List<Configuration> find(@Nonnull Predicate<String> descriptorFileFilter) {

        try (final JarFile jar = new JarFile(this.dataSourcePath.toFile())) {
            return jar
                    .stream()
                    .filter(entry -> descriptorFileFilter.test(getEntryName(entry)))
                    .map(entry -> convertEntryToFile(jar, entry))
                    .findAny()
                    .map(file -> new Configuration(file, metadataReader.read(jar)))
                    .stream()
                    .toList();
        } catch (IOException e) {
            throw new CodeGraphDataSourceException(e);
        }
    }

    @Override
    public String toString() {
        return "CodeGraphJarDataSource{" + "dataSourcePath=" + dataSourcePath + '}';
    }

    @Nonnull
    protected String getEntryName(@Nonnull JarEntry entry) {
        return entry.getName();
    }

    protected File convertEntryToFile(final JarFile jar, final JarEntry entry) {

        try (final InputStream is = jar.getInputStream(entry)) {
            final File tempEntryFile = File.createTempFile(UUID.randomUUID().toString(), null);
            tempEntryFile.deleteOnExit();

            Files.copy(is, tempEntryFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return tempEntryFile;
        } catch (IOException ex) {
            throw new CodeGraphDataSourceException(ex);
        }
    }
}
