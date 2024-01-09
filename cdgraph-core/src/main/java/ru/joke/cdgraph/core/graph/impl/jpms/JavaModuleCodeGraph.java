package ru.joke.cdgraph.core.graph.impl.jpms;

import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.datasources.CodeGraphDataSourceException;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.graph.GraphTag;
import ru.joke.cdgraph.core.graph.impl.*;

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

/**
 * Implementation of a modules graph based on Java modules (the descriptor is 'module-info.class').
 *
 * @author Alik
 * @see CodeGraph
 * @see CodeGraphDataSource
 */
public final class JavaModuleCodeGraph extends AbstractCodeGraph<AbstractCodeGraph.Context> {

    public static final String MAIN_CLASS_TAG = "main-class";
    public static final String MODULE_INFO_CLASS = "module-info.class";
    public static final String IS_SYNTHETIC_TAG = "synthetic";

    public static final String SYNTHETIC_RELATION_TYPE = "synthetic";
    public static final String REQUIRES_RELATION_TYPE = "requires";

    public JavaModuleCodeGraph(@Nonnull CodeGraphDataSource dataSource) {
        super(dataSource, new AbstractCodeGraph.Context() {});
    }

    private JavaModuleCodeGraph(@Nonnull GraphNode rootNode, @Nonnull Map<String, GraphNode> nodes) {
        super(rootNode, nodes);
    }

    @Override
    @Nonnull
    public CodeGraph clone(@Nonnull CloneOptions... options) {
        final var nodesCopies = cloneGraphNodes(options);
        final var rootNodeCopy = nodesCopies.get(findRootNode().id());

        return new JavaModuleCodeGraph(rootNodeCopy, nodesCopies);
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(
            @Nonnull CodeGraphDataSource dataSource,
            @Nonnull AbstractCodeGraph.Context context) {

        final Map<String, GraphNode> nodes = new HashMap<>();

        final List<CodeGraphDataSource.Configuration> moduleConfigs = dataSource.find(MODULE_INFO_CLASS::equals);
        moduleConfigs.forEach(moduleConfig -> {
            final ModuleDescriptor descriptor = parseModuleConfig(moduleConfig.descriptor());

            final Map<String, GraphTag<?>> moduleTags = collectModuleTags(moduleConfig, descriptor, true);
            final GraphNode sourceNode = nodes.computeIfAbsent(
                    descriptor.name(),
                    nodeId -> new SimpleGraphNode(nodeId, new HashSet<>(), new HashMap<>())
            );

            sourceNode.tags().putAll(moduleTags);

            final Set<GraphNodeRelation> relations =
                    descriptor.requires()
                                .stream()
                                .map(dependency -> buildRelation(sourceNode, dependency, nodes))
                                .collect(Collectors.toSet());

            sourceNode.relations().addAll(relations);
        });

        return nodes;
    }

    @Override
    protected GraphNode findOrCreateSingleRootNode(
            @Nonnull Map<String, GraphNode> nodesMap,
            @Nonnull Set<String> rootNodesIds,
            @Nonnull CodeGraphDataSource dataSource) {
        if (rootNodesIds.size() <= 1) {
            return super.findOrCreateSingleRootNode(nodesMap, rootNodesIds, dataSource);
        }

        final Map<String, GraphTag<?>> tags = Map.of(IS_SYNTHETIC_TAG, new SimpleGraphTag<>(IS_SYNTHETIC_TAG, true));
        final GraphNode earNode = new SimpleGraphNode(dataSource.id(), new HashSet<>(), tags);

        rootNodesIds
                .stream()
                .map(nodesMap::get)
                .map(node -> new SimpleGraphNodeRelation(earNode, node, new SimpleRelationType(SYNTHETIC_RELATION_TYPE), tags))
                .forEach(earNode.relations()::add);

        nodesMap.putIfAbsent(earNode.id(), earNode);

        return earNode;
    }

    private GraphNodeRelation buildRelation(
            final GraphNode sourceNode,
            final ModuleDescriptor.Requires dependency,
            final Map<String, GraphNode> nodesMap) {

        final GraphNode targetNode = nodesMap.get(dependency.name()) == null
                ? buildDependentNode(dependency, nodesMap)
                : nodesMap.get(dependency.name());
        nodesMap.putIfAbsent(targetNode.id(), targetNode);

        return new SimpleGraphNodeRelation(
                sourceNode,
                targetNode,
                new SimpleRelationType(REQUIRES_RELATION_TYPE),
                collectRelationTags(dependency)
        );
    }

    private GraphNode buildDependentNode(
            final ModuleDescriptor.Requires dependency,
            final Map<String, GraphNode> nodesMap) {

        final ModuleFinder finder = ModuleFinder.ofSystem();
        final var module = finder
                            .find(dependency.name())
                            .map(ModuleReference::descriptor);
        final Map<String, GraphTag<?>> tags = module
                                                .map(m -> collectModuleTags(null, m, false))
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

    private Map<String, GraphTag<?>> collectModuleTags(
            final CodeGraphDataSource.Configuration moduleConfig,
            final ModuleDescriptor descriptor,
            final boolean isSourceModule) {

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
        if (moduleConfig != null && !moduleConfig.classesMetadata().isEmpty()) {
            tags.put(CLASSES_METADATA_TAG, new SimpleGraphTag<>(CLASSES_METADATA_TAG, moduleConfig.classesMetadata()));
        }

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
