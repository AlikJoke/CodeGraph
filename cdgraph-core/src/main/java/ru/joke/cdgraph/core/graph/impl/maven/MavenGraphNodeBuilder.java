package ru.joke.cdgraph.core.graph.impl.maven;

import org.apache.maven.api.model.Model;
import ru.joke.cdgraph.core.graph.impl.AbstractCodeGraph;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphNode;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;
import ru.joke.cdgraph.core.meta.ClassMetadata;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphTag;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.joke.cdgraph.core.graph.impl.maven.MavenModuleCodeGraph.*;

final class MavenGraphNodeBuilder {

    @Nonnull
    GraphNode build(
            @Nonnull String id,
            @Nonnull Model model,
            @Nonnull Set<ClassMetadata> metadata) {
        return new SimpleGraphNode(id, new HashSet<>(), createModuleTags(model, metadata));
    }

    private Map<String, GraphTag<?>> createModuleTags(final Model model, final Set<ClassMetadata> metadata) {
        final Set<GraphTag<?>> tags = new HashSet<>(7);
        final String version = model.getVersion() == null ? model.getParent().getVersion() : model.getVersion();
        tags.add(new SimpleGraphTag<>(AbstractCodeGraph.VERSION_TAG, version));
        tags.add(new SimpleGraphTag<>(AbstractCodeGraph.SOURCE_MODULE_TAG, true));

        if (model.getName() != null) {
            tags.add(new SimpleGraphTag<>(MODULE_NAME_TAG, model.getName()));
        }

        if (model.getDescription() != null) {
            tags.add(new SimpleGraphTag<>(MODULE_DESCRIPTION_TAG, model.getDescription()));
        }

        final String groupId = model.getGroupId() == null ? model.getParent().getGroupId() : model.getGroupId();
        tags.add(new SimpleGraphTag<>(MODULE_GROUP_TAG, groupId));

        if (model.getPackaging() != null) {
            tags.add(new SimpleGraphTag<>(MODULE_PACKAGING_TAG, model.getPackaging()));
        }

        if (!metadata.isEmpty()) {
            tags.add(new SimpleGraphTag<>(AbstractCodeGraph.CLASSES_METADATA_TAG, metadata));
        }

        return tags
                .stream()
                .collect(Collectors.toMap(GraphTag::name, Function.identity()));
    }
}
