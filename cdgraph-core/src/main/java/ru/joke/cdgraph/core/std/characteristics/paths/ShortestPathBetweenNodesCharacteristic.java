package ru.joke.cdgraph.core.std.characteristics.paths;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.std.characteristics.SimpleCodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Implementation of the “shortest path between two modules” characteristic based on the wave algorithm
 * for finding the shortest path between two vertices of the graph.
 *
 * @author Alik
 */
public final class ShortestPathBetweenNodesCharacteristic implements CodeGraphCharacteristic<PathBetweenNodes> {

    private final PathBetweenNodesCharacteristicParameters parameters;

    public ShortestPathBetweenNodesCharacteristic(@Nonnull PathBetweenNodesCharacteristicParameters parameters) {
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<PathBetweenNodes> compute(@Nonnull CodeGraph graph) {
        final var sourceNode = graph.findNodeById(this.parameters.sourceNodeId())
                                    .orElseThrow(() -> new CodeGraphComputationException("Node '" + this.parameters.sourceNodeId() + "' not found"));
        if (graph.findNodeById(this.parameters.targetNodeId()).isEmpty()) {
            throw new CodeGraphComputationException("Node '" + this.parameters.sourceNodeId() + "' not found");
        }

        final Map<GraphNode, List<GraphNodeRelation>> relationsByTarget = new HashMap<>();
        final List<GraphNodeRelation> relationsInPath = scanFrontRelations(List.of(sourceNode), relationsByTarget);

        return buildComputationResult(sourceNode, relationsInPath);
    }

    private CodeGraphCharacteristicResult<PathBetweenNodes> buildComputationResult(
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

        final var resultData = new PathBetweenNodes(relationsInPath, nodesInPath);
        return new SimpleCodeGraphCharacteristicResult<>(resultData) {
            @Override
            public String toJson() {
                final var nodesIdsInPath =
                        get().nodesInPath()
                                .stream()
                                .map(GraphNode::id)
                                .toList();
                return gson.toJson(nodesIdsInPath);
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

                if (this.parameters.targetNodeId().equals(relation.target().id())) {
                    return createRelationsTraceFromSourceToTarget(relation.target(), relationsByTarget);
                }

                if (relation.type() != GraphNodeRelation.RelationType.PROVIDED) {
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
            final GraphNodeRelation traceRelation = traceRelations.get(0);
            relationsInPath.add(traceRelation);

            traceNode = traceRelation.source();
            traceRelations = relationsByTarget.get(traceNode);
        }

        Collections.reverse(relationsInPath);

        return relationsInPath;
    }
}
