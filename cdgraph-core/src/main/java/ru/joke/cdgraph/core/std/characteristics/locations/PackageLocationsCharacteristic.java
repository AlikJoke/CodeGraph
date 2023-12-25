package ru.joke.cdgraph.core.std.characteristics.locations;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.std.characteristics.SimpleCodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.std.AbstractCodeGraph.CLASSES_METADATA_TAG;
import static ru.joke.cdgraph.core.std.AbstractCodeGraph.SOURCE_MODULE_TAG;

public final class PackageLocationsCharacteristic implements CodeGraphCharacteristic<Set<String>> {

    private final PackageLocationsCharacteristicParameters parameters;

    public PackageLocationsCharacteristic(@Nonnull PackageLocationsCharacteristicParameters parameters) {
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Set<String>> compute(@Nonnull CodeGraph graph) {
        final var modulesWithPackage =
                graph.findAllNodes()
                        .stream()
                        .filter(this::isSourceModule)
                        .filter(this::doesModuleContainPackage)
                        .map(GraphNode::id)
                        .collect(Collectors.toSet());
        return new SimpleCodeGraphCharacteristicResult<>(modulesWithPackage);
    }

    private boolean isSourceModule(final GraphNode node) {
        @SuppressWarnings("unchecked")
        final var isSourceTag = (GraphTag<Boolean>) node.tags().get(SOURCE_MODULE_TAG);
        return isSourceTag != null && isSourceTag.value();
    }

    private boolean doesModuleContainPackage(final GraphNode node) {

        @SuppressWarnings("unchecked")
        final var metadataTag = (GraphTag<Set<ClassMetadata>>) node.tags().get(CLASSES_METADATA_TAG);
        final Set<ClassMetadata> classesMetadata =
                metadataTag == null || metadataTag.value().isEmpty()
                        ? Collections.emptySet()
                        : metadataTag.value();

        return classesMetadata
                    .stream()
                    .anyMatch(metadata -> metadata.packageName().equals(this.parameters.packageName()));
    }
}
