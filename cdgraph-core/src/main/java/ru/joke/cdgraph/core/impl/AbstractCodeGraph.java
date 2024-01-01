package ru.joke.cdgraph.core.impl;

import ru.joke.cdgraph.core.*;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class AbstractCodeGraph implements CodeGraph {

    public static final String SOURCE_MODULE_TAG = "source";
    public static final String VERSION_TAG = "version";
    public static final String CLASSES_METADATA_TAG = "classes-metadata";

    protected final Map<String, GraphNode> nodes;
    protected final GraphNode rootNode;

    protected AbstractCodeGraph(@Nonnull CodeGraphDataSource dataSource) {
        final Map<String, GraphNode> nodesMap = buildNodesMap(dataSource);
        this.nodes = Map.copyOf(nodesMap);

        final Set<String> allNodeIds = new HashSet<>(nodesMap.keySet());
        nodesMap.values()
                    .forEach(node -> removeDependentNodesFromMap(node, allNodeIds));

        this.rootNode = findRootNode(nodesMap, allNodeIds, dataSource);
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

    protected abstract Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource);

    protected GraphNode findRootNode(
            @Nonnull Map<String, GraphNode> nodesMap,
            @Nonnull Set<String> rootNodesIds,
            @Nonnull CodeGraphDataSource dataSource) {
        if (rootNodesIds.size() != 1) {
            throw new CodeGraphConfigurationException("Found more than 1 root module: " + rootNodesIds.size());
        }

        return nodesMap.get(rootNodesIds.iterator().next());
    }

    protected Map<String, GraphTag<?>> collectModuleClassesMetadataTags(@Nonnull Set<ClassMetadata> classesMetadata) {
        return classesMetadata.isEmpty()
                ? Collections.emptyMap()
                : Map.of(CLASSES_METADATA_TAG, new SimpleGraphTag<>(CLASSES_METADATA_TAG, classesMetadata));
    }

    private void removeDependentNodesFromMap(final GraphNode node, final Set<String> allNodesIds) {
        node.relations()
                .forEach(dependency -> allNodesIds.remove(dependency.target().id()));
    }
}
