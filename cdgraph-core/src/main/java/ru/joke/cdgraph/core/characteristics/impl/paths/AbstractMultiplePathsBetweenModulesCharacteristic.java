package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicParameters;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.characteristics.impl.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

abstract class AbstractMultiplePathsBetweenModulesCharacteristic<T extends CodeGraphCharacteristicParameters> extends
        VisualizedPathBetweenModulesCharacteristicBase<List<PathBetweenModules>> {

    protected final String id;
    protected final T parameters;

    AbstractMultiplePathsBetweenModulesCharacteristic(
            @Nonnull final String id,
            final T parameters) {
        this.id = id;
        this.parameters = parameters;
    }

    protected CodeGraphCharacteristicResult<List<PathBetweenModules>> buildComputationResult(
            final CodeGraph graph,
            final List<List<GraphNodeRelation>> allPaths) {

        final var resultPaths = allPaths
                                    .stream()
                                    .sorted(Comparator.comparingInt(List::size))
                                    .map(this::createOneResultPath)
                                    .toList();

        return new SimpleCodeGraphCharacteristicResult<>(this.id, this.parameters, resultPaths) {
            @Override
            @Nonnull
            public String toJson() {
                final var nodesIdsInPath =
                        get().stream()
                                .map(path -> path.modulesInPath()
                                                    .stream()
                                                    .map(GraphNode::id)
                                                    .toList()
                                )
                                .toList();
                return toJson(nodesIdsInPath);
            }

            @Nonnull
            @Override
            public CodeGraph visualizedGraph() {
                final var graphCopy = graph.clone(CodeGraph.CloneOptions.CLEAR_TAGS);
                for (int i = 0; i < resultPaths.size(); i++) {
                    addTagsToPathComponentsInGraphCopy(graphCopy, resultPaths.get(i), i + 1);
                }

                return graphCopy;
            }
        };
    }

    private PathBetweenModules createOneResultPath(final List<GraphNodeRelation> path) {

        final List<GraphNode> nodesInPath = new ArrayList<>(1 + path.size());
        nodesInPath.add(path.getFirst().source());

        path.stream()
                .map(GraphNodeRelation::target)
                .forEach(nodesInPath::add);

        return new PathBetweenModules(path, nodesInPath);
    }
}
