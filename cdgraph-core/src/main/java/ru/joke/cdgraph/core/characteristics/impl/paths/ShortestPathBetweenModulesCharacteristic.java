package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicComputationException;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * A characteristic that computes shortest path between two modules of the graph.<br>
 * Implementation of the “shortest path between two modules” characteristic based on
 * the wave algorithm for finding the shortest path between two vertices of the graph.<br>
 * Type of the characteristic parameters: {@link PathBetweenModulesCharacteristicParameters}.
 *
 * @author Alik
 *
 * @see ShortestPathBetweenModulesCharacteristicFactory
 * @see ShortestPathBetweenModulesCharacteristicFactoryDescriptor
 */
final class ShortestPathBetweenModulesCharacteristic extends VisualizedPathBetweenModulesCharacteristicBase<PathBetweenModules> {

    private final String id;
    private final PathBetweenModulesCharacteristicParameters parameters;

    public ShortestPathBetweenModulesCharacteristic(
            @Nonnull String id,
            @Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        this.parameters = parameters;
        this.id = id;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<PathBetweenModules> compute(@Nonnull CodeGraph graph) {
        final var sourceNode = graph.findNodeById(this.parameters.sourceModuleId())
                                    .orElseThrow(() -> new CodeGraphCharacteristicComputationException("Node '" + this.parameters.sourceModuleId() + "' not found"));
        if (graph.findNodeById(this.parameters.targetModuleId()).isEmpty()) {
            throw new CodeGraphCharacteristicComputationException("Node '" + this.parameters.sourceModuleId() + "' not found");
        }

        final Map<GraphNode, List<GraphNodeRelation>> relationsByTarget = new HashMap<>();
        final List<GraphNodeRelation> relationsInPath = scanFrontRelations(List.of(sourceNode), relationsByTarget);

        return buildComputationResult(graph, sourceNode, relationsInPath);
    }

    private CodeGraphCharacteristicResult<PathBetweenModules> buildComputationResult(
            final CodeGraph graph,
            final GraphNode sourceNode,
            final List<GraphNodeRelation> relationsInPath) {

        final List<GraphNode> nodesInPath = new ArrayList<>(1 + relationsInPath.size());
        if (!relationsInPath.isEmpty()) {
            nodesInPath.add(sourceNode);
        }
        relationsInPath
                .stream()
                .map(GraphNodeRelation::target)
                .forEach(nodesInPath::add);

        final var resultData = new PathBetweenModules(relationsInPath, nodesInPath);
        return new SimpleCodeGraphCharacteristicResult<>(this.id, this.parameters, resultData) {
            @Override
            @Nonnull
            public String toJson() {
                final var nodesIdsInPath =
                        get().modulesInPath()
                                .stream()
                                .map(GraphNode::id)
                                .toList();
                return toJson(nodesIdsInPath);
            }

            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                final var graphCopy = graph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
                addTagsToPathComponentsInGraphCopy(graphCopy, resultData, 1);

                return graphCopy;
            }
        };
    }

    private List<GraphNodeRelation> scanFrontRelations(
            final List<GraphNode> frontNodes,
            final Map<GraphNode, List<GraphNodeRelation>> relationsByTarget) {

        final List<GraphNode> newFrontNodes = new ArrayList<>();

        for (final GraphNode frontNode : frontNodes) {

            for (final GraphNodeRelation relation : frontNode.relations()) {

                // if we have already visited a given node earlier, then we do not need to go through its relations again
                if (relationsByTarget.containsKey(relation.target())) {
                    continue;
                }

                relationsByTarget.computeIfAbsent(relation.target(), node -> new ArrayList<>()).add(relation);

                if (this.parameters.targetModuleId().equals(relation.target().id())) {
                    return createRelationsTraceFromSourceToTarget(relation.target(), relationsByTarget);
                }

                if (relation.type().isTransitive()) {
                    newFrontNodes.add(relation.target());
                }
            }
        }

        return newFrontNodes.isEmpty() ? Collections.emptyList() : scanFrontRelations(newFrontNodes, relationsByTarget);
    }

    private List<GraphNodeRelation> createRelationsTraceFromSourceToTarget(
            final GraphNode targetNode,
            final Map<GraphNode, List<GraphNodeRelation>> relationsByTarget) {
        GraphNode traceNode = targetNode;
        List<GraphNodeRelation> traceRelations = relationsByTarget.get(traceNode);

        final List<GraphNodeRelation> relationsInPath = new ArrayList<>();
        while (traceRelations != null && !traceRelations.isEmpty()) {
            final GraphNodeRelation traceRelation = traceRelations.getFirst();
            relationsInPath.add(traceRelation);

            traceNode = traceRelation.source();
            traceRelations = relationsByTarget.get(traceNode);
        }

        Collections.reverse(relationsInPath);

        return relationsInPath;
    }
}
