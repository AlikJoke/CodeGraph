package ru.joke.cdgraph.core.graph.impl;

import ru.joke.cdgraph.core.datasources.CodeGraphDataSource;
import ru.joke.cdgraph.core.graph.*;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCodeGraph<T extends AbstractCodeGraph.Context> implements CodeGraph {

    public static final String SOURCE_MODULE_TAG = "source";
    public static final String VERSION_TAG = "version";
    public static final String CLASSES_METADATA_TAG = "classes-metadata";

    protected final Map<String, GraphNode> nodes;
    protected final GraphNode rootNode;

    public AbstractCodeGraph(@Nonnull Map<String, GraphNode> nodes) {
        this.rootNode = findOrCreateSingleRootNode(nodes, findRootNodesIds(nodes));
        this.nodes = Map.copyOf(nodes);
    }

    protected AbstractCodeGraph(@Nonnull GraphNode rootNode, @Nonnull Map<String, GraphNode> nodes) {
        this.rootNode = rootNode;
        this.nodes = Collections.unmodifiableMap(nodes);
    }

    protected AbstractCodeGraph(@Nonnull CodeGraphDataSource dataSource, @Nonnull T context) {
        final Map<String, GraphNode> nodesMap = buildNodesMap(dataSource, context);
        this.rootNode = findOrCreateSingleRootNode(nodesMap, findRootNodesIds(nodesMap), dataSource);
        this.nodes = Map.copyOf(nodesMap);
    }

    @Nonnull
    @Override
    public GraphNode findRootNode() {
        return this.rootNode;
    }

    @Nonnull
    @Override
    public Optional<GraphNode> findNodeById(@Nonnull String id) {
        return Optional.ofNullable(this.nodes.get(id));
    }

    @Nonnull
    @Override
    public Collection<GraphNode> findAllNodes() {
        return this.nodes.values();
    }

    protected abstract Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource, @Nonnull T context);

    protected GraphNode findOrCreateSingleRootNode(
            @Nonnull Map<String, GraphNode> nodesMap,
            @Nonnull Set<String> rootNodesIds,
            @Nonnull CodeGraphDataSource dataSource) {
        return findOrCreateSingleRootNode(nodesMap, rootNodesIds);
    }

    @Nonnull
    protected Map<String, GraphNode> cloneGraphNodes(@Nonnull CloneOptions... options) {

        final var optionsSet = options.length == 0
                ? EnumSet.noneOf(CloneOptions.class)
                : EnumSet.of(options[0], options);
        final boolean clearTags = optionsSet.contains(CloneOptions.CLEAR_TAGS);
        final boolean clearMetadataTags = optionsSet.contains(CloneOptions.CLEAR_CLASSES_METADATA);

        final Collection<GraphNode> sourceNodes = findAllNodes();
        final Map<String, GraphNode> nodesCopies = new HashMap<>(sourceNodes.size(), 1);
        for (final GraphNode sourceNode : sourceNodes) {

            final var sourceNodeCopy = nodesCopies.computeIfAbsent(
                    sourceNode.id(),
                    id -> copyGraphNode(sourceNode, clearTags, clearMetadataTags)
            );

            for (final GraphNodeRelation relation : sourceNode.relations()) {

                final var targetNode = relation.target();
                final var targetNodeCopy = nodesCopies.computeIfAbsent(
                        targetNode.id(),
                        id -> copyGraphNode(targetNode, clearTags, clearMetadataTags)
                );

                final var relationCopy = new SimpleGraphNodeRelation(
                        sourceNodeCopy,
                        targetNodeCopy,
                        relation.type(),
                        clearTags ? new HashMap<>(2) : new HashMap<>(relation.tags())
                );
                sourceNodeCopy.relations().add(relationCopy);
            }
        }

        return nodesCopies;
    }


    private Set<String> findRootNodesIds(final Map<String, GraphNode> nodes) {
        final Set<String> allNodeIds = new HashSet<>(nodes.keySet());
        nodes.values()
                .forEach(node -> removeDependentNodesFromMap(node, allNodeIds));

        return allNodeIds;
    }

    private GraphNode findOrCreateSingleRootNode(
            @Nonnull Map<String, GraphNode> nodesMap,
            @Nonnull Set<String> rootNodesIds) {
        if (rootNodesIds.size() != 1) {
            throw new CodeGraphConfigurationException("Found more than 1 root module: " + rootNodesIds.size());
        }

        return nodesMap.get(rootNodesIds.iterator().next());
    }

    private void removeDependentNodesFromMap(final GraphNode node, final Set<String> allNodesIds) {
        node.relations()
                .forEach(dependency -> allNodesIds.remove(dependency.target().id()));
    }

    private GraphNode copyGraphNode(
            final GraphNode sourceNode,
            final boolean clearTags,
            final boolean clearMetadataTags) {
        final Map<String, GraphTag<?>> tags =
                clearTags
                        ? new HashMap<>(2)
                        : clearMetadataTags
                            ? createTagsCopyWithoutMetadata(sourceNode.tags())
                            : new HashMap<>(sourceNode.tags());
        return new SimpleGraphNode(
                sourceNode.id(),
                new HashSet<>(),
                tags
        );
    }

    private Map<String, GraphTag<?>> createTagsCopyWithoutMetadata(final Map<String, GraphTag<?>> tags) {
        return tags.entrySet()
                    .stream()
                    .filter(entry -> !CLASSES_METADATA_TAG.equals(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public interface Context {}
}
