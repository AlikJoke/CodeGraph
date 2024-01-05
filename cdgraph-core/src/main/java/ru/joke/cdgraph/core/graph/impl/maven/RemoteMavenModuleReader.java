package ru.joke.cdgraph.core.graph.impl.maven;

import org.apache.maven.api.model.Dependency;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Parent;
import org.apache.maven.model.v4.MavenStaxReader;
import ru.joke.cdgraph.core.meta.ClassMetadata;
import ru.joke.cdgraph.core.meta.ClassesMetadataReader;
import ru.joke.cdgraph.core.graph.CodeGraphConfigurationException;

import javax.annotation.Nonnull;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarFile;

import static ru.joke.cdgraph.core.graph.impl.maven.MavenModuleCodeGraph.POM;

final class RemoteMavenModuleReader {

    private static final String JAR = "jar";

    private final HttpClient httpClient;
    private final String mavenRepositoryBaseUrl;
    private final ClassesMetadataReader<JarFile> classesMetadataReader;
    private final MavenStaxReader modelReader;

    private RemoteMavenModuleReader(
            @Nonnull HttpClient httpClient,
            @Nonnull String mavenRepositoryBaseUrl,
            @Nonnull ClassesMetadataReader<JarFile> classesMetadataReader,
            @Nonnull MavenStaxReader modelReader) {
        this.httpClient = httpClient;
        this.mavenRepositoryBaseUrl = mavenRepositoryBaseUrl;
        this.classesMetadataReader = classesMetadataReader;
        this.modelReader = modelReader;
    }

    @Nonnull
    Set<ClassMetadata> readClassesMetadata(@Nonnull Dependency dependency) {

        File jarFile = null;
        try {
            jarFile = File.createTempFile(UUID.randomUUID().toString(), "." + JAR);
            downloadJarToFile(jarFile, dependency);

            return this.classesMetadataReader.read(new JarFile(jarFile, false));
        } catch (IOException ex) {
            throw new CodeGraphConfigurationException(ex);
        } finally {
            if (jarFile != null) {
                jarFile.delete();
            }
        }
    }

    Model readParentModuleModel(@Nonnull Parent parent) {
        final var uri = composeArtifactModelUri(parent.getGroupId(), parent.getArtifactId(), parent.getVersion());
        return readModuleModel(uri, true);
    }

    @Nonnull
    Model readModuleModel(@Nonnull Dependency dependency) {
        final String artifactUri = composeArtifactModelUri(dependency);
        return Objects.requireNonNull(readModuleModel(artifactUri, false), "model");
    }

    private Model readModuleModel(@Nonnull String uri, boolean skipErrors) {
        try (final var modelStream = executeRequest(uri, skipErrors)) {
            return modelStream == null ? null : modelReader.read(modelStream);
        } catch (XMLStreamException | IOException | InterruptedException e) {
            throw new CodeGraphConfigurationException(e);
        }
    }

    private void downloadJarToFile(final File jarFile, final Dependency dependency) {
        final String uri = composeArtifactJarUri(dependency);

        try (final InputStream dependencyStream = executeRequest(uri);
                final FileOutputStream fos = new FileOutputStream(jarFile)) {
            fos.write(dependencyStream.readAllBytes());
        } catch (IOException | InterruptedException e) {
            throw new CodeGraphConfigurationException(e);
        }
    }

    private InputStream executeRequest(final String uri) throws IOException, InterruptedException {
        return executeRequest(uri, false);
    }

    private InputStream executeRequest(final String uri, final boolean skipErrors) throws IOException, InterruptedException {
        final URI url = URI.create(this.mavenRepositoryBaseUrl + uri);
        final var request = HttpRequest
                                .newBuilder()
                                    .GET()
                                    .uri(url)
                                    .timeout(Duration.ofSeconds(10))
                                .build();
        final var response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        final boolean success = response.statusCode() == 200;
        if (!success && !skipErrors) {
            throw new CodeGraphConfigurationException("Unable to request artifact descriptor from repo: " + response);
        }

        return success ? response.body() : null;
    }

    private String composeArtifactJarUri(final Dependency dependency) {
        return composeArtifactUri(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), JAR);
    }

    private String composeArtifactModelUri(final Dependency dependency) {
        return composeArtifactUri(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(), POM);
    }

    private String composeArtifactModelUri(final String groupId, final String artifactId, final String version) {
        return composeArtifactUri(groupId, artifactId, version, POM);
    }

    private String composeArtifactUri(
            final String groupId,
            final String artifactId,
            final String version,
            final String dataType) {
        return groupId.replace(".", "/")
                + "/"
                + artifactId
                + "/"
                + version
                + "/"
                + (artifactId + "-" + version + "." + dataType);
    }

    @Nonnull
    static Builder builder() {
        return new Builder();
    }

    static class Builder {

        private HttpClient httpClient;
        private String mavenRepositoryBaseUrl;
        private ClassesMetadataReader<JarFile> classesMetadataReader;
        private MavenStaxReader mavenModelReader;

        @Nonnull
        Builder withHttpClient(@Nonnull HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        @Nonnull
        Builder withMavenRepositoryBaseUrl(@Nonnull String mavenRepositoryBaseUrl) {
            this.mavenRepositoryBaseUrl = mavenRepositoryBaseUrl;
            return this;
        }

        @Nonnull
        Builder withClassesMetadataReader(@Nonnull ClassesMetadataReader<JarFile> classesMetadataReader) {
            this.classesMetadataReader = classesMetadataReader;
            return this;
        }

        @Nonnull
        Builder withMavenModelReader(@Nonnull MavenStaxReader mavenModelReader) {
            this.mavenModelReader = mavenModelReader;
            return this;
        }

        @Nonnull
        public RemoteMavenModuleReader build() {
            return new RemoteMavenModuleReader(this.httpClient, this.mavenRepositoryBaseUrl, this.classesMetadataReader, this.mavenModelReader);
        }
    }
}
