package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

abstract class AbstractSingleResourceLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<GraphNode>> {

    private static final String RESOURCE_NAME_TAG = "resource-name";
    private static final String RESOURCE_EXIST_TAG = "contains-resource";

    protected final String[] resourceNameParts;

    AbstractSingleResourceLocationsCharacteristic(
            @Nonnull String id,
            @Nonnull ResourceLocationsCharacteristicParameters parameters) {
        super(id, parameters);
        this.resourceNameParts = parameters.resourceName().split("\\.");
    }

    @Nonnull
    protected Set<GraphNode> transformResult(@Nonnull Set<GraphNode> modules) {
        return modules;
    }

    @Nonnull
    protected Object transformResultToJsonFormat(@Nonnull Set<GraphNode> result) {
        return convertToIds(result);
    }

    @Nonnull
    @Override
    protected CodeGraph transformResultToVisualizedGraph(@Nonnull CodeGraph codeGraph, @Nonnull Set<GraphNode> result) {
        return createTaggedGraphCopy(codeGraph, result, String.join(".", this.resourceNameParts));
    }

    @Nonnull
    protected CodeGraph createTaggedGraphCopy(
            @Nonnull CodeGraph codeGraph,
            @Nonnull Set<GraphNode> modulesWithResources,
            @Nonnull String resourceName) {
        final var graphCopy = codeGraph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
        modulesWithResources
                .stream()
                .map(GraphNode::id)
                .map(graphCopy::findNodeById)
                .flatMap(Optional::stream)
                .forEach(node -> addNodeTags(node, resourceName));

        return graphCopy;
    }

    private void addNodeTags(final GraphNode node, final String resourceName) {
        final var tagClass = new SimpleGraphTag<>(RESOURCE_NAME_TAG, resourceName);
        node.tags().put(tagClass.name(), tagClass);

        final var containsClass = new SimpleGraphTag<>(RESOURCE_EXIST_TAG, true);
        node.tags().put(containsClass.name(), containsClass);
    }
}
