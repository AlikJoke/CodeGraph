package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.meta.ClassMetadata;

import javax.annotation.Nonnull;

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
final class PackageLocationsCharacteristic extends AbstractSingleResourceLocationsCharacteristic {

    PackageLocationsCharacteristic(
            @Nonnull String id,
            @Nonnull ResourceLocationsCharacteristicParameters parameters) {
        super(id, parameters);
    }

    @Override
    protected boolean isResourceSatisfied(@Nonnull final GraphNode module, @Nonnull final ClassMetadata metadata) {
        final String[] packageComponents = metadata.packageName().split("\\.");
        final int targetPackageComponentsLength = this.resourceNameParts.length;
        for (int i = 0; i < packageComponents.length && i < targetPackageComponentsLength; i++) {
            if (!packageComponents[i].equals(this.resourceNameParts[i])) {
                return false;
            }
        }

        return true;
    }
}
