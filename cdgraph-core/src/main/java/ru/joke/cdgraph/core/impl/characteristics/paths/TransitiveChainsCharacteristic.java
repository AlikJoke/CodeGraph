package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.CodeGraphComputationException;
import ru.joke.cdgraph.core.GraphNodeRelation;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

final class TransitiveChainsCharacteristic extends AbstractMultiplePathsBetweenModulesCharacteristic<SingleModuleCharacteristicParameters> {

    TransitiveChainsCharacteristic(
            @Nonnull final String id,
            @Nonnull final SingleModuleCharacteristicParameters parameters) {
        super(id, parameters);
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<List<PathBetweenModules>> compute(@Nonnull CodeGraph graph) {
        final var sourceNode = graph.findNodeById(this.parameters.moduleId())
                                    .orElseThrow(() -> new CodeGraphComputationException("Node '" + this.parameters.moduleId() + "' not found"));

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
