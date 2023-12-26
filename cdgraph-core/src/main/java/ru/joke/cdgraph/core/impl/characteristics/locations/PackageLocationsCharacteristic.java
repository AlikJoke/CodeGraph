package ru.joke.cdgraph.core.impl.characteristics.locations;

import com.google.gson.Gson;
import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

public final class PackageLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<GraphNode>> {

    private final String[] packageParts;

    public PackageLocationsCharacteristic(@Nonnull ResourceLocationsCharacteristicParameters parameters) {
        this.packageParts = parameters.resourceName().split("\\.");
    }

    @Override
    protected boolean isResourceSatisfied(@Nonnull final GraphNode module, @Nonnull final ClassMetadata metadata) {
        final String[] packageComponents = metadata.packageName().split("\\.");
        final int targetPackageComponentsLength = this.packageParts.length;
        for (int i = 0; i < packageComponents.length && i < targetPackageComponentsLength; i++) {
            if (!packageComponents[i].equals(this.packageParts[i])) {
                return false;
            }
        }

        return true;
    }

    @Nonnull
    protected Set<GraphNode> transformResult(@Nonnull Set<GraphNode> modules) {
        return modules;
    }

    @Nonnull
    protected String transformResultToJson(@Nonnull Gson gson, @Nonnull Set<GraphNode> result) {
        final var modulesIds = convertToIds(result);
        return gson.toJson(modulesIds);
    }
}
