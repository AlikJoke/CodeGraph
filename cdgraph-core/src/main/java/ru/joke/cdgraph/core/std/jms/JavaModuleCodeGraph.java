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

    public static final String REQUIRES_TYPE = "requires";

    public static final String MAIN_CLASS_TAG = "main-class";
    public static final String VERSION_TAG = "version";

    private static final String MODULE_INFO_CLASS = "module-info.class";

    public JavaModuleCodeGraph(@Nonnull CodeGraphDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource) {

        final Map<String, GraphNode> nodes = new HashMap<>();

        final List<File> moduleConfigs = dataSource.find(MODULE_INFO_CLASS::equals);
        for (final File moduleConfig : moduleConfigs) {
            final ModuleDescriptor descriptor = parseModuleConfig(moduleConfig);

            final Set<GraphTag<?>> tags = collectModuleTags(descriptor);
            final GraphNode sourceNode = nodes.computeIfAbsent(
                    descriptor.name(),
                    nodeId -> new SimpleGraphNode(nodeId, new HashSet<>(), new HashSet<>())
            );

            sourceNode.tags().addAll(tags);

            final Set<GraphNodeRelation> relations =
                    descriptor.requires()
                                .stream()
                                .map(dependency -> buildRelation(sourceNode, dependency, nodes))
                                .collect(Collectors.toSet());

            sourceNode.relations().addAll(relations);
        }

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

        return new SimpleGraphNodeRelation(sourceNode, targetNode, REQUIRES_TYPE, collectRelationTags(dependency));
    }

    private GraphNode buildDependentNode(
            final ModuleDescriptor.Requires dependency,
            final Map<String, GraphNode> nodesMap) {

        final ModuleFinder finder = ModuleFinder.ofSystem();
        final var module = finder
                            .find(dependency.name())
                            .map(ModuleReference::descriptor);
        final Set<GraphTag<?>> tags = module
                                    .map(this::collectModuleTags)
                                    .orElseGet(HashSet::new);

        final GraphNode sourceNode = new SimpleGraphNode(dependency.name(), new HashSet<>(), tags);
        module.map(ModuleDescriptor::requires)
                .stream()
                .flatMap(Set::stream)
                .map(relationDependency -> buildRelation(sourceNode, relationDependency, nodesMap))
                .forEach(sourceNode.relations()::add);
        return sourceNode;
    }

    private Set<GraphTag<?>> collectRelationTags(final ModuleDescriptor.Requires dependency) {

        final Set<GraphTag<?>> relationTags = new HashSet<>(dependency.modifiers().size() + 1);
        dependency.rawCompiledVersion()
                    .ifPresent(version -> relationTags.add(new SimpleGraphTag<>(VERSION_TAG, version)));
        dependency.modifiers()
                    .stream()
                    .map(modifier -> new SimpleGraphTag<>(modifier.name().toLowerCase(), true))
                    .forEach(relationTags::add);

        return relationTags;
    }

    private Set<GraphTag<?>> collectModuleTags(final ModuleDescriptor descriptor) {

        final Set<GraphTag<?>> tags = new HashSet<>(2 + descriptor.modifiers().size());

        descriptor.mainClass()
                    .ifPresent(cls -> tags.add(new SimpleGraphTag<>(MAIN_CLASS_TAG, cls)));
        descriptor.rawVersion()
                    .ifPresent(version -> tags.add(new SimpleGraphTag<>(VERSION_TAG, version)));
        descriptor.modifiers()
                    .stream()
                    .map(modifier -> new SimpleGraphTag<>(modifier.name().toLowerCase(), true))
                    .forEach(tags::add);

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
