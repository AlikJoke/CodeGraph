package ru.joke.cdgraph.core.impl.characteristics.locations;

import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A characteristic that computes duplicates of classes between different graph modules.
 * Allows to find conflicting versions of classes that have the same qualified class name.
 * This avoids problems with a situation where an application ends up with two classes with
 * the same qualified name, causing the ClassLoader to load only one class, which can
 * cause problems with the application.
 *
 * @author Alik
 *
 * @see AllClassesDuplicatesLocationsCharacteristicFactory
 * @see AllClassesDuplicatesLocationsCharacteristicFactoryHandle
 */
final class AllClassesDuplicatesLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<ClassDuplicatesLocations>> {

    private final Map<String, Set<GraphNode>> class2moduleMap = new HashMap<>();

    AllClassesDuplicatesLocationsCharacteristic(@Nonnull String id) {
        super(id, null);
    }

    @Override
    protected boolean isResourceSatisfied(@Nonnull GraphNode module, @Nonnull ClassMetadata metadata) {
        final String classQualifiedName = metadata.packageName() + "." + metadata.className();
        final var modules = this.class2moduleMap.computeIfAbsent(classQualifiedName, clsName -> new HashSet<>(1, 1));
        modules.add(module);

        return modules.size() > 1;
    }

    @Override
    @Nonnull
    protected Set<ClassDuplicatesLocations> transformResult(@Nonnull Set<GraphNode> modules) {
        final var entries = this.class2moduleMap.entrySet();
        return entries
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(entry -> new ClassDuplicatesLocations(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }

    @Override
    @Nonnull
    protected Object transformResultToJsonFormat(@Nonnull Set<ClassDuplicatesLocations> result) {
        return result
                .stream()
                .map(location ->
                        Map.of("className", location.classQualifiedName(), "modules", convertToIds(location.modules()))
                )
                .collect(Collectors.toSet());
    }

}
