package ru.joke.cdgraph.core.impl.characteristics.bridges;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.paths.AllPathsBetweenModulesCharacteristic;
import ru.joke.cdgraph.core.impl.characteristics.paths.PathBetweenModules;
import ru.joke.cdgraph.core.impl.characteristics.paths.PathBetweenModulesCharacteristicParameters;
import ru.joke.cdgraph.core.impl.characteristics.paths.TransitiveChainsCharacteristic;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class DependencyBridgesCharacteristic implements CodeGraphCharacteristic<Set<GraphNodeRelation>> {

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Set<GraphNodeRelation>> compute(@Nonnull CodeGraph graph) {
        final var candidatesToBridge =
                graph.findAllNodes()
                        .stream()
                        .map(GraphNode::relations)
                        .flatMap(Set::stream)
                        .collect(Collectors.groupingBy(GraphNodeRelation::target))
                        .values()
                            .stream()
                            .filter(graphNodeRelations -> graphNodeRelations.size() == 1)
                            .flatMap(List::stream)
                            .collect(Collectors.toSet());

        final Set<GraphNodeRelation> bridges = new HashSet<>();
        candidatesToBridge.forEach(candidate -> {
            final Set<GraphNode> nodesInPath = findAllNodesInPathAfterBridgeRelation(candidate, graph);
            final GraphNode rootNode = graph.findRootNode();

            if (!nodesInPath.isEmpty() &&
                    nodesInPath
                        .stream()
                        .noneMatch(targetNode -> isNodeHasMultiplePathsFromRoot(targetNode, rootNode, graph))) {
                bridges.add(candidate);
            }
        });

        return new SimpleCodeGraphCharacteristicResult<>(bridges) {
            @Override
            public String toJson() {
                final var bridgesMaps = convertBridgesToMap(bridges);
                return gson.toJson(bridgesMaps);
            }
        };
    }

    private Set<Map<String, String>> convertBridgesToMap(final Set<GraphNodeRelation> bridges) {
        return bridges
                .stream()
                .map(bridge -> Map.of("source", bridge.source().id(), "target", bridge.target().id()))
                .collect(Collectors.toSet());
    }

    private Set<GraphNode> findAllNodesInPathAfterBridgeRelation(final GraphNodeRelation bridgeCandidate, final CodeGraph graph) {

        final String targetNodeId = bridgeCandidate.target().id();
        final var params = new SingleModuleCharacteristicParameters(targetNodeId);
        final var transitiveChainsCharacteristic = new TransitiveChainsCharacteristic(params);

        final var allPathsFromTargetResult = transitiveChainsCharacteristic.compute(graph);
        final var allPathsFromTarget = allPathsFromTargetResult.get();

        return allPathsFromTarget
                .stream()
                .map(PathBetweenModules::modulesInPath)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
    }

    private boolean isNodeHasMultiplePathsFromRoot(final GraphNode targetNode, final GraphNode rootNode, final CodeGraph graph) {
        final var pathsBetweenCharacteristicParameters = new PathBetweenModulesCharacteristicParameters(rootNode.id(), targetNode.id());
        final var allPathsBetweenNodesCharacteristic = new AllPathsBetweenModulesCharacteristic(pathsBetweenCharacteristicParameters);

        final var allPathsBetweenNodesResult = allPathsBetweenNodesCharacteristic.compute(graph);
        final var allPathsBetweenNodes = allPathsBetweenNodesResult.get();
        return allPathsBetweenNodes.size() > 1;
    }
}
