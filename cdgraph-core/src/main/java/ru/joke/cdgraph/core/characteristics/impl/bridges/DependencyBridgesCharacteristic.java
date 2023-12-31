package ru.joke.cdgraph.core.characteristics.impl.bridges;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicService;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SingleModuleCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.paths.AllPathsBetweenModulesCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.characteristics.impl.paths.PathBetweenModules;
import ru.joke.cdgraph.core.characteristics.impl.paths.PathBetweenModulesCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.impl.paths.TransitiveChainsCharacteristicFactoryDescriptor;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A characteristic that computes graph bridges. Graph bridges are edges that connect
 * the blocks of the graph. Removing such an edge leads to loss of connectivity of the graph.<br>
 * This characteristic makes it possible to determine dependencies, the presence of which
 * leads to the appearance of a new block of the graph. Getting rid of such edges
 * (if possible, if the dependency is obtained transitively and is not needed for the
 * application functionality) will eliminate such blocks and reduce the size of the
 * application distribution.
 *
 * @author Alik
 *
 * @see DependencyBridgesCharacteristicFactory
 * @see DependencyBridgesCharacteristicFactoryDescriptor
 */
final class DependencyBridgesCharacteristic implements CodeGraphCharacteristic<Set<GraphNodeRelation>> {

    private static final String BRIDGE_TAG = "bridge";

    private final String id;
    private final CodeGraphCharacteristicService registry;

    DependencyBridgesCharacteristic(
            @Nonnull String id,
            @Nonnull CodeGraphCharacteristicService registry) {
        this.id = id;
        this.registry = registry;
    }

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

        return new SimpleCodeGraphCharacteristicResult<>(this.id, bridges) {
            @Override
            @Nonnull
            public String toJson() {
                final var bridgesMaps = convertBridgesToMap(bridges);
                return toJson(bridgesMaps);
            }

            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                final var graphCopy = graph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
                bridges.forEach(bridge -> addBridgeTagInGraphCopy(bridge, graphCopy));

                return graphCopy;
            }

            private void addBridgeTagInGraphCopy(final GraphNodeRelation bridge, final CodeGraph graphCopy) {
                graphCopy.findNodeById(bridge.source().id())
                            .stream()
                            .map(GraphNode::relations)
                            .flatMap(Set::stream)
                            .filter(relation -> relation.target().equals(bridge.target()))
                            .findAny()
                            .ifPresent(this::addBridgeTag);
            }

            private void addBridgeTag(final GraphNodeRelation relation) {
                final var bridgeTag = new SimpleGraphTag<>(BRIDGE_TAG, true);
                relation.tags().put(bridgeTag.name(), bridgeTag);
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
        final CodeGraphCharacteristicFactory<CodeGraphCharacteristic<List<PathBetweenModules>>, List<PathBetweenModules>, SingleModuleCharacteristicParameters> transitiveChainsCharacteristicFactory =
                this.registry.findFactory(TransitiveChainsCharacteristicFactoryDescriptor.class);
        final var transitiveChainsCharacteristic = transitiveChainsCharacteristicFactory.createCharacteristic(params);

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
        final CodeGraphCharacteristicFactory<CodeGraphCharacteristic<List<PathBetweenModules>>, List<PathBetweenModules>, PathBetweenModulesCharacteristicParameters> allPathsBetweenNodesCharacteristicFactory =
                this.registry.findFactory(AllPathsBetweenModulesCharacteristicFactoryDescriptor.class);

        final var allPathsBetweenNodesCharacteristic = allPathsBetweenNodesCharacteristicFactory.createCharacteristic(pathsBetweenCharacteristicParameters);

        final var allPathsBetweenNodesResult = allPathsBetweenNodesCharacteristic.compute(graph);

        final List<PathBetweenModules> allPathsBetweenNodes = allPathsBetweenNodesResult.get();
        return allPathsBetweenNodes.size() > 1 && isLastPathPartDiffers(allPathsBetweenNodes);
    }

    private boolean isLastPathPartDiffers(final List<PathBetweenModules> paths) {
        final Set<GraphNodeRelation> lastPathParts = new HashSet<>();
        for (final var path : paths) {
            if (lastPathParts.add(path.relationsInPath().getLast()) && lastPathParts.size() > 1) {
                return true;
            }
        }

        return false;
    }
}
