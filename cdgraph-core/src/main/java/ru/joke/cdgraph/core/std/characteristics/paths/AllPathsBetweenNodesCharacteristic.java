package ru.joke.cdgraph.core.std.characteristics.paths;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.std.characteristics.SimpleCodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

public final class AllPathsBetweenNodesCharacteristic implements CodeGraphCharacteristic<List<PathBetweenNodes>> {

    private final PathBetweenNodesCharacteristicParameters parameters;

    public AllPathsBetweenNodesCharacteristic(@Nonnull PathBetweenNodesCharacteristicParameters parameters) {
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<List<PathBetweenNodes>> compute(@Nonnull CodeGraph graph) {
        final var sourceNode = graph.findNodeById(this.parameters.sourceNodeId())
                                    .orElseThrow(() -> new CodeGraphComputationException("Node '" + this.parameters.sourceNodeId() + "' not found"));
        if (graph.findNodeById(this.parameters.targetNodeId()).isEmpty()) {
            throw new CodeGraphComputationException("Node '" + this.parameters.sourceNodeId() + "' not found");
        }

        final List<List<GraphNodeRelation>> allPaths = new ArrayList<>();
        sourceNode.relations()
                    .forEach(relation -> scanSourceRelation(relation, allPaths));

        return buildComputationResult(allPaths);
    }

    private void scanSourceRelation(
            final GraphNodeRelation relation,
            final List<List<GraphNodeRelation>> allPaths) {

        final List<GraphNodeRelation> relationsInPath = new ArrayList<>();
        relationsInPath.add(relation);
        if (!relation.target().id().equals(this.parameters.targetNodeId())) {
            scanRelations(relation, relationsInPath, allPaths);
        } else {
            allPaths.add(relationsInPath);
        }
    }

    private CodeGraphCharacteristicResult<List<PathBetweenNodes>> buildComputationResult(final List<List<GraphNodeRelation>> allPaths) {

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

    private boolean scanRelations(
            final GraphNodeRelation relation,
            final List<GraphNodeRelation> relationsInPath,
            final List<List<GraphNodeRelation>> allPaths) {

        for (final GraphNodeRelation nextRelation : relation.target().relations()) {

            relationsInPath.add(nextRelation);

            if (nextRelation.target().id().equals(this.parameters.targetNodeId())
                    || scanRelations(nextRelation, relationsInPath, allPaths)) {
                allPaths.add(new ArrayList<>(relationsInPath));

                relationsInPath.remove(nextRelation);
            }
        }

        relationsInPath.remove(relation);

        return false;
    }
}
