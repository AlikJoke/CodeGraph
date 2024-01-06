package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.meta.ClassMetadata;
import ru.joke.cdgraph.core.graph.GraphNode;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * A characteristic that computes locations of the package (set of modules) with specified name.
 * Allows to find duplicates of packages that have the same name in different modules
 * (split packages in JPMS).<br>
 * Type of the characteristic parameters: {@link ResourceLocationsCharacteristicParameters}.
 *
 * @author Alik
 *
 * @see PackageLocationsCharacteristicFactory
 * @see PackageLocationsCharacteristicFactoryDescriptor
 */
final class PackageLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<GraphNode>> {

    private final String[] packageParts;

    PackageLocationsCharacteristic(
            @Nonnull String id,
            @Nonnull ResourceLocationsCharacteristicParameters parameters) {
        super(id, parameters);
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
    protected Object transformResultToJsonFormat(@Nonnull Set<GraphNode> result) {
        return convertToIds(result);
    }
}
