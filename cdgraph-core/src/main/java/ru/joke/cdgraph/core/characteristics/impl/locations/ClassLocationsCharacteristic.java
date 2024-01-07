package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.meta.ClassMetadata;

import javax.annotation.Nonnull;

/**
 * A characteristic that computes locations of the class (set of modules) with specified name.
 * Allows to find duplicates of classes that have the same qualified class name.<br>
 * Type of the characteristic parameters: {@link ResourceLocationsCharacteristicParameters}.
 *
 * @author Alik
 *
 * @see ClassLocationsCharacteristicFactory
 * @see ClassLocationsCharacteristicFactoryDescriptor
 */
final class ClassLocationsCharacteristic extends AbstractSingleResourceLocationsCharacteristic {

    public ClassLocationsCharacteristic(
            @Nonnull String id,
            @Nonnull ResourceLocationsCharacteristicParameters parameters) {
        super(id, parameters);
    }

    @Override
    protected boolean isResourceSatisfied(@Nonnull final GraphNode module, @Nonnull final ClassMetadata metadata) {
        final String[] packageComponents = metadata.packageName().split("\\.");
        final int targetClassComponentsLength = this.resourceNameParts.length;
        if (packageComponents.length != targetClassComponentsLength - 1) {
            return false;

        }
        for (int i = 0; i < packageComponents.length && i < targetClassComponentsLength - 1; i++) {
            if (!packageComponents[i].equals(this.resourceNameParts[i])) {
                return false;
            }
        }

        return metadata.className().equals(this.resourceNameParts[targetClassComponentsLength - 1]);
    }
}
