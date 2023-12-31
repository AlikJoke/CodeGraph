package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphTag;
import ru.joke.cdgraph.core.meta.ClassMetadata;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.graph.impl.AbstractCodeGraph.CLASSES_METADATA_TAG;
import static ru.joke.cdgraph.core.graph.impl.AbstractCodeGraph.SOURCE_MODULE_TAG;

abstract class AbstractResourceLocationsCharacteristic<T> implements CodeGraphCharacteristic<T> {

    protected final String id;
    protected final CodeGraphCharacteristicParameters parameters;

    AbstractResourceLocationsCharacteristic(
            @Nonnull String id,
            @Nullable CodeGraphCharacteristicParameters parameters) {
        this.id = id;
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<T> compute(@Nonnull CodeGraph graph) {
        final var modulesWithResource =
                graph.findAllNodes()
                        .stream()
                        .filter(this::isSourceModule)
                        .filter(this::doesModuleContainResource)
                        .collect(Collectors.toSet());
        return new SimpleCodeGraphCharacteristicResult<>(this.id, this.parameters, transformResult(modulesWithResource)) {
            @Override
            @Nonnull
            public String toJson() {
                return toJson(transformResultToJsonFormat(get()));
            }

            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                return transformResultToVisualizedGraph(graph, get());
            }
        };
    }

    protected abstract boolean isResourceSatisfied(@Nonnull GraphNode module, @Nonnull ClassMetadata metadata);

    @Nonnull
    protected abstract T transformResult(@Nonnull Set<GraphNode> modules);

    @Nonnull
    protected abstract Object transformResultToJsonFormat(@Nonnull T result);

    @Nonnull
    protected abstract CodeGraph transformResultToVisualizedGraph(@Nonnull CodeGraph codeGraph, @Nonnull T result);

    @Nonnull
    protected Set<String> convertToIds(@Nonnull Set<GraphNode> nodes) {
        return nodes
                .stream()
                .map(GraphNode::id)
                .collect(Collectors.toSet());
    }

    private boolean isSourceModule(final GraphNode node) {
        @SuppressWarnings("unchecked")
        final var isSourceTag = (GraphTag<Boolean>) node.tags().get(SOURCE_MODULE_TAG);
        return isSourceTag != null && isSourceTag.value();
    }

    private boolean doesModuleContainResource(final GraphNode node) {

        @SuppressWarnings("unchecked")
        final var metadataTag = (GraphTag<Set<ClassMetadata>>) node.tags().get(CLASSES_METADATA_TAG);
        final Set<ClassMetadata> classesMetadata =
                metadataTag == null || metadataTag.value().isEmpty()
                        ? Collections.emptySet()
                        : metadataTag.value();

        return classesMetadata
                    .stream()
                    .anyMatch(metadata -> isResourceSatisfied(node, metadata));
    }
}
