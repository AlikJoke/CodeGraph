package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public record PathBetweenModules(
        @Nonnull List<GraphNodeRelation> relationsInPath,
        @Nonnull List<GraphNode> modulesInPath) {

    @Override
    public String toString() {
        final String path = modulesInPath
                                .stream()
                                .map(GraphNode::id)
                                .collect(Collectors.joining(" -> "));
        return "PathBetweenModules: [%s]".formatted(path);
    }
}
