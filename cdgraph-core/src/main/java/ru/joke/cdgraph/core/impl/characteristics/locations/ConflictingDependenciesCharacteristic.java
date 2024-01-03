package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.impl.AbstractCodeGraph.VERSION_TAG;

/**
 * A characteristic that computes conflicting dependencies of the module in the code graph.
 * Allows to find conflicting versions of the modules that have the same identifier but
 * different versions.<br>
 * This helps avoid situations where a class from a library of the wrong version is loaded.
 *
 * @author Alik
 *
 * @see ConflictingDependenciesCharacteristicFactory
 * @see ConflictingDependenciesCharacteristicFactoryHandle
 */
final class ConflictingDependenciesCharacteristic implements CodeGraphCharacteristic<Set<ConflictingDependencies>> {

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
                final Set<Set<String>> modulesIds = result
                                                        .stream()
                                                        .map(ConflictingDependenciesCharacteristic.this::convertNodesToIds)
                                                        .collect(Collectors.toSet());
                return toJson(modulesIds);
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
