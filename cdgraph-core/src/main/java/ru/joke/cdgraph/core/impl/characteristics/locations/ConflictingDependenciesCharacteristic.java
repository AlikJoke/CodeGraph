package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.impl.AbstractCodeGraph.VERSION_TAG;

public final class ConflictingDependenciesCharacteristic implements CodeGraphCharacteristic<Set<ConflictingDependencies>> {

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
        return new SimpleCodeGraphCharacteristicResult<>(result) {
            @Override
            public String toJson() {
                final Set<Set<String>> modulesIds = result
                                                        .stream()
                                                        .map(ConflictingDependenciesCharacteristic.this::convertNodesToIds)
                                                        .collect(Collectors.toSet());
                return gson.toJson(modulesIds);
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
