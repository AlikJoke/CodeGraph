package ru.joke.cdgraph.core.std.jms;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.std.AbstractCodeGraph;
import ru.joke.cdgraph.core.std.SimpleGraphNode;
import ru.joke.cdgraph.core.std.SimpleGraphNodeRelation;
import ru.joke.cdgraph.core.std.SimpleGraphTag;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.*;
import java.util.stream.Collectors;

public final class JavaModuleCodeGraph extends AbstractCodeGraph {

    public static final String MAIN_CLASS_TAG = "main-class";
    public static final String MODULE_INFO_CLASS = "module-info.class";

    public JavaModuleCodeGraph(@Nonnull CodeGraphDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource) {

        final Map<String, GraphNode> nodes = new HashMap<>();

        final List<CodeGraphDataSource.Configuration> moduleConfigs = dataSource.find(MODULE_INFO_CLASS::equals);
        moduleConfigs.forEach(moduleConfig -> {
            final ModuleDescriptor descriptor = parseModuleConfig(moduleConfig.descriptor());

            final Map<String, GraphTag<?>> moduleTags = collectModuleTags(descriptor, true);
            final GraphNode sourceNode = nodes.computeIfAbsent(
                    descriptor.name(),
                    nodeId -> new SimpleGraphNode(nodeId, new HashSet<>(), new HashMap<>())
            );

            sourceNode.tags().putAll(moduleTags);

            final var classesMetadataTags = collectModuleClassesMetadataTags(moduleConfig.classesMetadata());
            sourceNode.tags().putAll(classesMetadataTags);

            final Set<GraphNodeRelation> relations =
                    descriptor.requires()
                                .stream()
                                .map(dependency -> buildRelation(sourceNode, dependency, nodes))
                                .collect(Collectors.toSet());

            sourceNode.relations().addAll(relations);
        });

        return nodes;
    }

    private GraphNodeRelation buildRelation(
            final GraphNode sourceNode,
            final ModuleDescriptor.Requires dependency,
            final Map<String, GraphNode> nodesMap) {

        final GraphNode targetNode = nodesMap.get(dependency.name()) == null
                ? buildDependentNode(dependency, nodesMap)
                : nodesMap.get(dependency.name());
        nodesMap.putIfAbsent(targetNode.id(), targetNode);

        return new SimpleGraphNodeRelation(sourceNode, targetNode, GraphNodeRelation.RelationType.REQUIRES, collectRelationTags(dependency));
    }

    private GraphNode buildDependentNode(
            final ModuleDescriptor.Requires dependency,
            final Map<String, GraphNode> nodesMap) {

        final ModuleFinder finder = ModuleFinder.ofSystem();
        final var module = finder
                            .find(dependency.name())
                            .map(ModuleReference::descriptor);
        final Map<String, GraphTag<?>> tags = module
                                                .map(m -> collectModuleTags(m, false))
                                                .orElseGet(HashMap::new);

        final GraphNode sourceNode = new SimpleGraphNode(dependency.name(), new HashSet<>(), tags);
        module.map(ModuleDescriptor::requires)
                .stream()
                .flatMap(Set::stream)
                .map(relationDependency -> buildRelation(sourceNode, relationDependency, nodesMap))
                .forEach(sourceNode.relations()::add);
        return sourceNode;
    }

    private Map<String, GraphTag<?>> collectRelationTags(final ModuleDescriptor.Requires dependency) {

        final Map<String, GraphTag<?>> relationTags = new HashMap<>();
        dependency.rawCompiledVersion()
                    .ifPresent(version -> relationTags.put(VERSION_TAG, new SimpleGraphTag<>(VERSION_TAG, version)));
        dependency.modifiers()
                    .stream()
                    .map(modifier -> new SimpleGraphTag<>(modifier.name().toLowerCase(), true))
                    .forEach(tag -> relationTags.put(tag.name(), tag));

        return relationTags;
    }

    private Map<String, GraphTag<?>> collectModuleTags(final ModuleDescriptor descriptor, final boolean isSourceModule) {

        final Map<String, GraphTag<?>> tags = new HashMap<>();

        descriptor.mainClass()
                    .ifPresent(cls -> tags.put(MAIN_CLASS_TAG, new SimpleGraphTag<>(MAIN_CLASS_TAG, cls)));
        descriptor.rawVersion()
                    .ifPresent(version -> tags.put(VERSION_TAG, new SimpleGraphTag<>(VERSION_TAG, version)));
        descriptor.modifiers()
                    .stream()
                    .map(modifier -> new SimpleGraphTag<>(modifier.name().toLowerCase(), true))
                    .forEach(tag -> tags.put(tag.name(), tag));

        tags.put(SOURCE_MODULE_TAG, new SimpleGraphTag<>(SOURCE_MODULE_TAG, isSourceModule));

        return tags;
    }

    private ModuleDescriptor parseModuleConfig(@Nonnull File config) {

        try (final InputStream is = new FileInputStream(config)) {
            return ModuleDescriptor.read(is);
        } catch (IOException e) {
            throw new CodeGraphDataSourceException(e);
        } finally {
            config.delete();
        }
    }
}
