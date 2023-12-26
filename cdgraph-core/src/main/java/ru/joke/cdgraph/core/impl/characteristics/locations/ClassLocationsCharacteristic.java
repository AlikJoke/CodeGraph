package ru.joke.cdgraph.core.impl.characteristics.locations;

import com.google.gson.Gson;
import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

public final class ClassLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<GraphNode>> {

    private final String[] classQualifiedNameParts;

    public ClassLocationsCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
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
    protected String transformResultToJson(@Nonnull Gson gson, @Nonnull Set<GraphNode> result) {
        final var moduleIds = convertToIds(result);
        return gson.toJson(moduleIds);
    }
}
