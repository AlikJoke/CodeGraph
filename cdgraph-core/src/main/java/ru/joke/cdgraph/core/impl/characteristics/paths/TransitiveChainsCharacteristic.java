package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.impl.characteristics.SingleNodeCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public final class TransitiveChainsCharacteristic extends AbstractMultiplePathsBetweenNodesCharacteristic {

    private final SingleNodeCharacteristicParameters parameters;

    public TransitiveChainsCharacteristic(@Nonnull final SingleNodeCharacteristicParameters parameters) {
        this.parameters = parameters;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<List<PathBetweenNodes>> compute(@Nonnull CodeGraph graph) {
        final var sourceNode = graph.findNodeById(this.parameters.nodeId())
                                    .orElseThrow(() -> new CodeGraphComputationException("Node '" + this.parameters.nodeId() + "' not found"));

        final List<List<GraphNodeRelation>> chains = new ArrayList<>();
        sourceNode.relations()
                    .forEach(relation -> scanSourceRelation(relation, chains));

        return buildComputationResult(chains);
    }

    private void scanSourceRelation(
            final GraphNodeRelation relation,
            final List<List<GraphNodeRelation>> chains) {

        final List<GraphNodeRelation> relationsInPath = new ArrayList<>();
        relationsInPath.add(relation);
        scanRelations(relation, relationsInPath, chains);
    }

    private void scanRelations(
            final GraphNodeRelation relation,
            final List<GraphNodeRelation> relationsInPath,
            final List<List<GraphNodeRelation>> chains) {

        final var targetNodeRelations = relation.target().relations();
        if (relation.type() == GraphNodeRelation.RelationType.PROVIDED || targetNodeRelations.isEmpty()) {
            chains.add(new ArrayList<>(relationsInPath));
            relationsInPath.remove(relation);

            return;
        }

        for (final GraphNodeRelation nextRelation : targetNodeRelations) {
            relationsInPath.add(nextRelation);
            scanRelations(nextRelation, relationsInPath, chains);
        }
    }
}
