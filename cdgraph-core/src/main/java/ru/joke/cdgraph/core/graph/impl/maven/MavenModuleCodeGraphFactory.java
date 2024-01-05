package ru.joke.cdgraph.core.graph.impl.maven;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.graph.CodeGraphFactory;
import ru.joke.cdgraph.core.meta.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.PasswordAuthentication;

/**
 * Implementation of a factory for creating a modules graph based on Maven modules.
 *
 * @author Alik
 * @see CodeGraphFactory
 * @see CodeGraph
 */
public final class MavenModuleCodeGraphFactory implements CodeGraphFactory {

    private static final String MAVEN_REPO_BASE_URL = "https://repo1.maven.org/maven2/";

    private final String mavenRepositoryBaseUrl;
    private final PasswordAuthentication mavenRepositoryCredentials;

    public MavenModuleCodeGraphFactory() {
        this(MAVEN_REPO_BASE_URL, null);
    }

    public MavenModuleCodeGraphFactory(
            @Nonnull String mavenRepositoryBaseUrl,
            @Nullable PasswordAuthentication mavenRepositoryCredentials) {
        this.mavenRepositoryBaseUrl = mavenRepositoryBaseUrl;
        this.mavenRepositoryCredentials = mavenRepositoryCredentials;
    }

    @Nonnull
    @Override
    public CodeGraph create(@Nonnull CodeGraphDataSource dataSource) {
        return new MavenModuleCodeGraph(dataSource, new JarClassesMetadataReader(), this.mavenRepositoryBaseUrl, this.mavenRepositoryCredentials);
    }
}
