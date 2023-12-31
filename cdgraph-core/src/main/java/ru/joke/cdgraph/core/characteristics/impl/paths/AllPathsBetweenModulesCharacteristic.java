package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicComputationException;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * A characteristic that computes all paths between two modules of the graph.
 * Allows to find all chains from which a dependency to the source module can be
 * transitively extracted.<br>
 * Type of the characteristic parameters: {@link PathBetweenModulesCharacteristicParameters}.
 *
 * @author Alik
 *
 * @see AllPathsBetweenModulesCharacteristicFactory
 * @see AllPathsBetweenModulesCharacteristicFactoryDescriptor
 */
final class AllPathsBetweenModulesCharacteristic extends AbstractMultiplePathsBetweenModulesCharacteristic<PathBetweenModulesCharacteristicParameters> {

    AllPathsBetweenModulesCharacteristic(
            @Nonnull String id,
            @Nonnull PathBetweenModulesCharacteristicParameters parameters) {
        super(id, parameters);
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<List<PathBetweenModules>> compute(@Nonnull CodeGraph graph) {
        final var sourceNode = graph.findNodeById(this.parameters.sourceModuleId())
                                    .orElseThrow(() -> new CodeGraphCharacteristicComputationException("Node '" + this.parameters.sourceModuleId() + "' not found"));
        if (graph.findNodeById(this.parameters.targetModuleId()).isEmpty()) {
            throw new CodeGraphCharacteristicComputationException("Node '" + this.parameters.sourceModuleId() + "' not found");
        }

        final List<List<GraphNodeRelation>> allPaths = new ArrayList<>();
        sourceNode.relations()
                    .forEach(relation -> scanSourceRelation(relation, allPaths));

        return buildComputationResult(graph, allPaths);
    }

    private void scanSourceRelation(
            final GraphNodeRelation relation,
            final List<List<GraphNodeRelation>> allPaths) {

        final List<GraphNodeRelation> relationsInPath = new ArrayList<>();
        relationsInPath.add(relation);
        if (relation.target().id().equals(this.parameters.targetModuleId())) {
            allPaths.add(relationsInPath);
        } else {
            scanRelations(relation, relationsInPath, allPaths);
        }
    }

    private boolean scanRelations(
            final GraphNodeRelation relation,
            final List<GraphNodeRelation> relationsInPath,
            final List<List<GraphNodeRelation>> allPaths) {

        if (!relation.type().isTransitive()) {
            relationsInPath.remove(relation);
            return true;
        }

        for (final GraphNodeRelation nextRelation : relation.target().relations()) {

            relationsInPath.add(nextRelation);

            if (nextRelation.target().id().equals(this.parameters.targetModuleId())
                    || scanRelations(nextRelation, relationsInPath, allPaths)) {
                allPaths.add(new ArrayList<>(relationsInPath));

                relationsInPath.remove(nextRelation);
            }
        }

        relationsInPath.remove(relation);

        return false;
    }
}
