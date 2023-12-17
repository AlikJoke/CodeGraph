package ru.joke.cdgraph.core.std.jms;

import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.GraphTag;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class JavaModuleCodeGraph extends AbstractCodeGraph {

    public static final String REQUIRES_TYPE = "requires";

    public static final String MAIN_CLASS_TAG = "main-class";
    public static final String VERSION_TAG = "version";

    public JavaModuleCodeGraph(@Nonnull CodeGraphDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource) {

        final Map<String, GraphNode> nodes = new HashMap<>();

        for (final File moduleConfig : dataSource) {
            final ModuleDescriptor descriptor = parseModuleConfig(moduleConfig);

            final Set<GraphTag> tags = collectModuleTags(descriptor);
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

            sourceNode.dependencies().addAll(relations);
        }

        return nodes;
    }

    private GraphNodeRelation buildRelation(
            final GraphNode sourceNode,
            final ModuleDescriptor.Requires dependency,
            final Map<String, GraphNode> nodesMap) {

        final GraphNode targetNode = nodesMap.computeIfAbsent(
                dependency.name(),
                nodeId -> buildDependentNode(dependency)
        );

        return new SimpleGraphNodeRelation(sourceNode, targetNode, REQUIRES_TYPE, collectRelationTags(dependency));
    }

    private GraphNode buildDependentNode(final ModuleDescriptor.Requires dependency) {

        final ModuleFinder finder = ModuleFinder.ofSystem();
        final Set<GraphTag> tags =
                finder.find(dependency.name())
                        .map(dependencyDescriptor -> collectModuleTags(dependencyDescriptor.descriptor()))
                        .orElseGet(HashSet::new);

        return new SimpleGraphNode(dependency.name(), new HashSet<>(), tags);
    }

    private Set<GraphTag> collectRelationTags(final ModuleDescriptor.Requires dependency) {

        final Set<GraphTag> relationTags = new HashSet<>(dependency.modifiers().size() + 1);
        dependency.rawCompiledVersion()
                    .ifPresent(version -> relationTags.add(new SimpleGraphTag(VERSION_TAG, version)));
        dependency.modifiers()
                    .stream()
                    .map(modifier -> new SimpleGraphTag(modifier.name().toLowerCase(), true))
                    .forEach(relationTags::add);

        return relationTags;
    }

    private Set<GraphTag> collectModuleTags(final ModuleDescriptor descriptor) {

        final Set<GraphTag> tags = new HashSet<>(2 + descriptor.modifiers().size());

        descriptor.mainClass()
                    .ifPresent(cls -> tags.add(new SimpleGraphTag(MAIN_CLASS_TAG, cls)));
        descriptor.rawVersion()
                    .ifPresent(version -> tags.add(new SimpleGraphTag(VERSION_TAG, version)));
        descriptor.modifiers()
                    .stream()
                    .map(modifier -> new SimpleGraphTag(modifier.name().toLowerCase(), true))
                    .forEach(tags::add);

        return tags;
    }

    private ModuleDescriptor parseModuleConfig(@Nonnull File config) {
        try (final InputStream is = new FileInputStream(config)) {
            return ModuleDescriptor.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
