package ru.joke.cdgraph.core.characteristics.impl.locations;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphTag;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;
import ru.joke.cdgraph.core.meta.ClassMetadata;

import javax.annotation.Nonnull;
import java.util.*;
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
 * @see AllClassesDuplicatesLocationsCharacteristicFactoryDescriptor
 */
final class AllClassesDuplicatesLocationsCharacteristic extends AbstractResourceLocationsCharacteristic<Set<ClassDuplicatesLocations>> {

    static final String CLASS_NAME_TAG = "class";

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

    @Nonnull
    @Override
    protected CodeGraph transformResultToVisualizedGraph(
            @Nonnull CodeGraph codeGraph,
            @Nonnull Set<ClassDuplicatesLocations> result) {

        final var graphCopy = codeGraph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
        result.forEach(location -> addClassTagToModules(graphCopy, location));

        return graphCopy;
    }

    private void addClassTagToModules(
            final CodeGraph graphCopy,
            final ClassDuplicatesLocations locations) {
        locations.modules()
                    .stream()
                    .map(GraphNode::id)
                    .map(graphCopy::findNodeById)
                    .flatMap(Optional::stream)
                    .forEach(node -> addClassTag(node, locations.classQualifiedName()));
    }

    private void addClassTag(final GraphNode node, final String className) {
        @SuppressWarnings("unchecked")
        final GraphTag<List<String>> tag =
                (GraphTag<List<String>>) node.tags().computeIfAbsent(
                        CLASS_NAME_TAG,
                        tagId -> new SimpleGraphTag<>(tagId, new ArrayList<>())
                );
        tag.value().add(className);
    }
}
