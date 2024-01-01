package ru.joke.cdgraph.core.impl.maven;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.CodeGraphFactory;
import ru.joke.cdgraph.core.impl.JarClassesMetadataReader;

import javax.annotation.Nonnull;

public final class MavenModuleCodeGraphFactory implements CodeGraphFactory {

    private static final String MAVEN_REPO_BASE_URL = "https://repo1.maven.org/maven2/";

    private final String mavenRepositoryBaseUrl;

    public MavenModuleCodeGraphFactory() {
        this(MAVEN_REPO_BASE_URL);
    }

    public MavenModuleCodeGraphFactory(@Nonnull String mavenRepositoryBaseUrl) {
        this.mavenRepositoryBaseUrl = mavenRepositoryBaseUrl;
    }

    @Nonnull
    @Override
    public CodeGraph create(@Nonnull CodeGraphDataSource dataSource) {
        return new MavenModuleCodeGraph(dataSource, new JarClassesMetadataReader(), this.mavenRepositoryBaseUrl);
    }
}
