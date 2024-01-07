package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphTag;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.graph.impl.AbstractCodeGraph.VERSION_TAG;

/**
 * A characteristic that computes conflicting dependencies of the module in the code graph.
 * Allows to find conflicting versions of the modules that have the same identifier but
 * different versions.<br>
 * This helps avoid situations where a class from a library of the wrong version is loaded.
 *
 * @author Alik
 *
 * @see ConflictingDependenciesCharacteristicFactory
 * @see ConflictingDependenciesCharacteristicFactoryDescriptor
 */
final class ConflictingDependenciesCharacteristic implements CodeGraphCharacteristic<Set<ConflictingDependencies>> {

    private static final String CONFLICTING_DEPENDENCIES_TAG = "conflicting-group";

    private final String id;

    ConflictingDependenciesCharacteristic(@Nonnull String id) {
        this.id = id;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Set<ConflictingDependencies>> compute(@Nonnull CodeGraph graph) {
        final Map<String, Set<GraphNode>> modulesMap =
                graph.findAllNodes()
                        .stream()
                        .collect(Collectors.groupingBy(this::getUnversionedModuleId, Collectors.toSet()));
        final var result =
                modulesMap.values()
                        .stream()
                        .filter(nodes -> nodes.size() > 1)
                        .map(ConflictingDependencies::new)
                        .collect(Collectors.toSet());
        return new SimpleCodeGraphCharacteristicResult<>(this.id, result) {
            @Override
            @Nonnull
            public String toJson() {
                final var modulesIds = result
                                        .stream()
                                        .map(ConflictingDependenciesCharacteristic.this::convertNodesToIds)
                                        .collect(Collectors.toSet());
                return toJson(modulesIds);
            }

            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                final var graphCopy = graph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);

                int groupIndex = 0;
                for (final var conflictingGroup : result) {
                    addConflictingGroupIndexTagsToGroupOfModules(conflictingGroup, groupIndex++);
                }

                return graphCopy;
            }

            private void addConflictingGroupIndexTagsToGroupOfModules(final ConflictingDependencies group, final int groupIndex) {
                group.conflictingDependencies()
                        .forEach(module -> addConflictingGroupIndexTagToNode(module, groupIndex));
            }

            private void addConflictingGroupIndexTagToNode(final GraphNode node, final int groupIndex) {
                final var tag = new SimpleGraphTag<>(CONFLICTING_DEPENDENCIES_TAG, groupIndex);
                node.tags().put(tag.name(), tag);
            }
        };
    }

    private Set<String> convertNodesToIds(final ConflictingDependencies dependencies) {
        final var conflictingDependencies = dependencies.conflictingDependencies();
        return conflictingDependencies
                    .stream()
                    .map(GraphNode::id)
                    .collect(Collectors.toSet());
    }

    private String getUnversionedModuleId(final GraphNode module) {
        @SuppressWarnings("unchecked")
        final GraphTag<String> versionTag = (GraphTag<String>) module.tags().get(VERSION_TAG);
        if (versionTag == null || versionTag.value().isEmpty()) {
            return module.id();
        }

        return module.id().replace(versionTag.value(), "");
    }
}
