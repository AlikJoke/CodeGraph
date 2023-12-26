package ru.joke.cdgraph.core.impl.characteristics.locations;

import com.google.gson.Gson;
import ru.joke.cdgraph.core.ClassMetadata;
import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class AllClassesDuplicatesLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<ClassDuplicatesLocations>> {

    private final Map<String, Set<GraphNode>> class2moduleMap = new HashMap<>();

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
    protected String transformResultToJson(@Nonnull Gson gson, @Nonnull Set<ClassDuplicatesLocations> result) {
        final var resultMaps =
                result
                    .stream()
                    .map(location ->
                            Map.of("className", location.classQualifiedName(), "modules", convertToIds(location.modules()))
                    )
                    .collect(Collectors.toSet());
        return gson.toJson(resultMaps);
    }

}
