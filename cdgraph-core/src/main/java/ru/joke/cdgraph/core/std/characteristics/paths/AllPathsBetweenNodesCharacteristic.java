package ru.joke.cdgraph.core.std.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.GraphNodeRelation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class AllPathsBetweenNodesCharacteristic extends AbstractMultiplePathsBetweenNodesCharacteristic {

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
        if (relation.target().id().equals(this.parameters.targetNodeId())) {
            allPaths.add(relationsInPath);
        } else {
            scanRelations(relation, relationsInPath, allPaths);
        }
    }

    private boolean scanRelations(
            final GraphNodeRelation relation,
            final List<GraphNodeRelation> relationsInPath,
            final List<List<GraphNodeRelation>> allPaths) {

        if (relation.type() == GraphNodeRelation.RelationType.PROVIDED) {
            relationsInPath.remove(relation);
            return false;
        }

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
