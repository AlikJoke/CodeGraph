package ru.joke.cdgraph.core.std.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.std.characteristics.SimpleCodeGraphCharacteristicResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

abstract class AbstractMultiplePathsBetweenNodesCharacteristic implements CodeGraphCharacteristic<List<PathBetweenNodes>> {

    protected CodeGraphCharacteristicResult<List<PathBetweenNodes>> buildComputationResult(final List<List<GraphNodeRelation>> allPaths) {

        final var resultPaths = allPaths
                                    .stream()
                                    .sorted(Comparator.comparingInt(List::size))
                                    .map(this::createOneResultPath)
                                    .toList();

        return new SimpleCodeGraphCharacteristicResult<>(resultPaths) {
            @Override
            public String toJson() {
                final var nodesIdsInPath =
                        get().stream()
                                .map(path -> path.nodesInPath()
                                                    .stream()
                                                    .map(GraphNode::id)
                                                    .toList()
                                )
                                .collect(Collectors.toList());
                return gson.toJson(nodesIdsInPath);
            }
        };
    }

    private PathBetweenNodes createOneResultPath(final List<GraphNodeRelation> path) {

        final List<GraphNode> nodesInPath = new ArrayList<>(1 + path.size());
        nodesInPath.add(path.get(0).source());

        path.stream()
                .map(GraphNodeRelation::target)
                .forEach(nodesInPath::add);

        return new PathBetweenNodes(path, nodesInPath);
    }
}
