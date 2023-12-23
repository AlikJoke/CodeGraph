package ru.joke.cdgraph.core.std.characteristics.paths;

import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public record ShortestPathBetweenNodes(
        @Nonnull List<GraphNodeRelation> relationsInPath,
        @Nonnull List<GraphNode> nodesInPath) {

    @Override
    public String toString() {
        final String path = nodesInPath
                                .stream()
                                .map(GraphNode::id)
                                .collect(Collectors.joining(" -> "));
        return "ShortestPathBetweenNodes: [%s]".formatted(path);
    }
}
