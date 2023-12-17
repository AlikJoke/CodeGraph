package ru.joke.cdgraph.core.std.ds;

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

    private final Path dataSourcePath;

    public CodeGraphJarDataSource(@Nonnull Path dataSourcePath) {
        this.dataSourcePath = dataSourcePath;
    }

    @Nonnull
    @Override
    public List<File> find(@Nonnull Predicate<String> filter) {

        try (final JarFile jar = new JarFile(this.dataSourcePath.toFile())) {
            return jar
                    .stream()
                    .filter(entry -> filter.test(entry.getName()))
                    .map(entry -> convertEntryToFile(jar, entry))
                    .toList();
        } catch (IOException e) {
            throw new CodeGraphDataSourceException(e);
        }
    }

    @Override
    public String toString() {
        return "CodeGraphJarDataSource{" + "dataSourcePath=" + dataSourcePath + '}';
    }

    private File convertEntryToFile(final JarFile jar, final JarEntry entry) {

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
