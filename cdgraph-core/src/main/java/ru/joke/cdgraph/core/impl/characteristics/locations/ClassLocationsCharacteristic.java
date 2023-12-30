package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

final class ClassLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<GraphNode>> {

    private final String[] classQualifiedNameParts;

    public ClassLocationsCharacteristic(
            @Nonnull String id,
            @Nonnull ResourceLocationsCharacteristicParameters parameters) {
        super(id, parameters);
        this.classQualifiedNameParts = parameters.resourceName().split("\\.");
    }

    @Override
    protected boolean isResourceSatisfied(@Nonnull final GraphNode module, @Nonnull final ClassMetadata metadata) {
        final String[] packageComponents = metadata.packageName().split("\\.");
        final int targetClassComponentsLength = this.classQualifiedNameParts.length;
        if (packageComponents.length != targetClassComponentsLength - 1) {
            return false;

        }
        for (int i = 0; i < packageComponents.length && i < targetClassComponentsLength - 1; i++) {
            if (!packageComponents[i].equals(this.classQualifiedNameParts[i])) {
                return false;
            }
        }

        return metadata.className().equals(this.classQualifiedNameParts[targetClassComponentsLength - 1]);
    }

    @Nonnull
    protected Set<GraphNode> transformResult(@Nonnull Set<GraphNode> modules) {
        return modules;
    }

    @Nonnull
    protected Object transformResultToJsonFormat(@Nonnull Set<GraphNode> result) {
        return convertToIds(result);
    }
}
