package ru.joke.cdgraph.core.impl.maven;

import org.apache.maven.api.model.Dependency;
import org.apache.maven.api.model.DependencyManagement;
import org.apache.maven.api.model.Model;
import ru.joke.cdgraph.core.CodeGraphConfigurationException;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.GraphTag;
import ru.joke.cdgraph.core.impl.SimpleGraphNodeRelation;
import ru.joke.cdgraph.core.impl.SimpleGraphTag;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.impl.maven.MavenModuleCodeGraph.*;

final class MavenModuleDependenciesReader {

    private static final String PROJECT_VERSION_PROPERTY = "${project.version}";

    private static final String TEST_SCOPE = "test";
    private static final String WAR_TYPE = "war";

    private final Map<String, Pair<GraphNode, Model>> modulesMap;
    private final Set<String> processedModules;
    private final RemoteMavenModuleReader remoteMavenModuleReader;
    private final MavenGraphNodeBuilder mavenGraphNodeBuilder;

    MavenModuleDependenciesReader(
            @Nonnull RemoteMavenModuleReader remoteMavenModuleReader,
            @Nonnull Map<String, Pair<GraphNode, Model>> modulesMap,
            @Nonnull Set<String> processedModules,
            @Nonnull MavenGraphNodeBuilder mavenGraphNodeBuilder) {
        this.remoteMavenModuleReader = remoteMavenModuleReader;
        this.modulesMap = modulesMap;
        this.processedModules = processedModules;
        this.mavenGraphNodeBuilder = mavenGraphNodeBuilder;
    }

    void read(@Nonnull Pair<GraphNode, Model> module, @Nonnull Set<String> exclusions) {

        final var moduleNode = module.first();
        if (!processedModules.add(moduleNode.id())) {
            return;
        }

        final Model moduleModel = module.second();
        final Set<Dependency> dependencies = new LinkedHashSet<>(moduleModel.getDependencies());
        final Map<String, Dependency> dependencyManagementMap = new HashMap<>();
        final Map<String, String> properties = collectProperties(moduleModel);

        collectDependenciesFromDependencyManagement(dependencyManagementMap, moduleModel.getDependencyManagement());
        collectDataFromParent(moduleModel, dependencies, dependencyManagementMap, properties);

        dependencies
                .stream()
                .filter(dependency -> !TEST_SCOPE.equals(dependency.getScope()))
                .filter(dependency -> !exclusions.contains(composeUnversionedDependencyId(dependency)))
                .map(dependency -> getVersionedDependency(dependency, dependencyManagementMap, properties))
                .forEach(dependency -> {

                    final String artifactFullId = createModuleId(dependency);
                    final GraphNodeRelation.RelationType relationType = getRelationType(dependency);

                    final Pair<GraphNode, Model> dependencyModule = modulesMap.computeIfAbsent(artifactFullId, id -> createModuleDataByDependency(id, dependency));
                    moduleNode.relations().add(new SimpleGraphNodeRelation(moduleNode, dependencyModule.first(), relationType, createDependencyTags(dependency)));

                    if (relationType != GraphNodeRelation.RelationType.PROVIDED) {
                        read(dependencyModule, collectDependencyExclusions(exclusions, dependency));
                    }
                });
    }

    private Map<String, String> collectProperties(final Model moduleModel) {
        final Map<String, String> properties = new HashMap<>(moduleModel.getProperties());
        if (moduleModel.getParent() != null) {
            properties.put(PROJECT_VERSION_PROPERTY, moduleModel.getParent().getVersion());
        }

        return properties;
    }

    private GraphNodeRelation.RelationType getRelationType(final Dependency dependency) {
        return dependency.getScope() == null
                ? GraphNodeRelation.RelationType.COMPILE
                : GraphNodeRelation.RelationType.valueOf(dependency.getScope().toUpperCase());
    }

    private Set<String> collectDependencyExclusions(@Nonnull Set<String> exclusions, @Nonnull Dependency dependency) {
        if (WAR_TYPE.equals(dependency.getType())) {
            return Collections.emptySet();
        }

        final Set<String> currentDependencyExclusions = new HashSet<>(exclusions);
        dependency.getExclusions()
                    .stream()
                    .map(exclusion -> composeUnversionedDependencyId(exclusion.getGroupId(), exclusion.getArtifactId()))
                    .forEach(currentDependencyExclusions::add);

        return currentDependencyExclusions;
    }

    private Pair<GraphNode, Model> createModuleDataByDependency(@Nonnull String artifactFullId, @Nonnull Dependency dependency) {

        final var dependencyModel = this.remoteMavenModuleReader.readModuleModel(dependency);
        final var classesMetadata = this.remoteMavenModuleReader.readClassesMetadata(dependency);
        final var dependencyModuleNode = this.mavenGraphNodeBuilder.build(artifactFullId, dependencyModel, classesMetadata);

        return new Pair<>(dependencyModuleNode, dependencyModel);
    }

    private Map<String, GraphTag<?>> createDependencyTags(final Dependency dependency) {
        final Set<GraphTag<?>> tags = new HashSet<>(4);

        if (dependency.getClassifier() != null) {
            tags.add(new SimpleGraphTag<>(CLASSIFIER_TAG, dependency.getClassifier()));
        }

        if (dependency.getType() != null) {
            tags.add(new SimpleGraphTag<>(DEPENDENCY_TYPE_TAG, dependency.getType()));
        }

        if (dependency.isOptional()) {
            tags.add(new SimpleGraphTag<>(OPTIONAL_TAG, true));
        }

        if (dependency.getSystemPath() != null) {
            tags.add(new SimpleGraphTag<>(SYSTEM_PATH_TAG, dependency.getSystemPath()));
        }

        return tags
                .stream()
                .collect(Collectors.toMap(GraphTag::name, Function.identity()));
    }

    private Dependency getVersionedDependency(
            final Dependency dependency,
            final Map<String, Dependency> dependencyManagementMap,
            final Map<String, String> properties) {

        if (dependency.getVersion() != null && !properties.containsKey(dependency.getVersion())) {
            return dependency;
        } else if (dependency.getVersion() != null) {
            return dependency.withVersion(properties.get(dependency.getVersion()));
        }

        final var unversionedArtifactId = composeUnversionedDependencyId(dependency);
        final var artifactVersionFromDependenciesManagement = dependencyManagementMap.get(unversionedArtifactId);

        if (artifactVersionFromDependenciesManagement != null) {
            final var version = properties.getOrDefault(artifactVersionFromDependenciesManagement.getVersion(), artifactVersionFromDependenciesManagement.getVersion());
            return dependency.withVersion(version);
        }

        throw new CodeGraphConfigurationException("Unable to find dependency version for " + dependency);
    }

    private void collectDataFromParent(
            final Model model,
            final Set<Dependency> dependencies,
            final Map<String, Dependency> dependencyManagementMap,
            final Map<String, String> properties) {

        var parent = model.getParent();
        while (parent != null) {
            final var parentModel = this.remoteMavenModuleReader.readParentModuleModel(parent);
            if (parentModel == null) {
                break;
            }

            final var dependencyManagement = parentModel.getDependencyManagement();
            collectDependenciesFromDependencyManagement(dependencyManagementMap, dependencyManagement);

            final var parentDependencies = parentModel.getDependencies();
            dependencies.addAll(parentDependencies);

            parentModel.getProperties().forEach(properties::putIfAbsent);

            parent = parentModel.getParent();
        }
    }

    private void collectDependenciesFromDependencyManagement(
            final Map<String, Dependency> dependencyManagementMap,
            final DependencyManagement dependencyManagement) {
        if (dependencyManagement == null) {
            return;
        }

        dependencyManagement.getDependencies().forEach(dependency -> {
            final var unversionedDependencyId = composeUnversionedDependencyId(dependency.getGroupId(), dependency.getArtifactId());
            dependencyManagementMap.putIfAbsent(unversionedDependencyId, dependency);
        });
    }

    private String composeUnversionedDependencyId(final String groupId, final String artifactId) {
        return groupId + ID_PART_DELIMITER + artifactId;
    }

    private String composeUnversionedDependencyId(final Dependency dependency) {
        return composeUnversionedDependencyId(dependency.getGroupId(), dependency.getArtifactId());
    }

    private String createModuleId(final Dependency dependency) {
        return createModuleId(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion());
    }

    private String createModuleId(final String groupId, final String artifactId, final String version) {
        return groupId + ID_PART_DELIMITER + artifactId + ID_PART_DELIMITER + version;
    }
}
