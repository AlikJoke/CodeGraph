package ru.joke.cdgraph.core.impl.characteristics.paths;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A characteristic that computes graph description in the form of all transitive
 * chains from the root node to all leaves.
 *
 * @author Alik
 *
 * @see CodeGraphDescriptionCharacteristicFactory
 * @see CodeGraphDescriptionCharacteristicFactoryHandle
 */
final class CodeGraphDescriptionCharacteristic implements CodeGraphCharacteristic<List<PathBetweenModules>> {

    private final String id;
    private final CodeGraphCharacteristicFactoryRegistry registry;

    CodeGraphDescriptionCharacteristic(
            @Nonnull String id,
            @Nonnull CodeGraphCharacteristicFactoryRegistry registry) {
        this.id = id;
        this.registry = registry;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<List<PathBetweenModules>> compute(@Nonnull CodeGraph graph) {
        final var rootNodeId = graph.findRootNode().id();
        final CodeGraphCharacteristicFactory<CodeGraphCharacteristic<List<PathBetweenModules>>, List<PathBetweenModules>, SingleModuleCharacteristicParameters> characteristicFactory =
                this.registry.find(TransitiveChainsCharacteristicFactoryHandle.class);
        final var params = new SingleModuleCharacteristicParameters(rootNodeId);
        final var transitiveChainsCharacteristic = characteristicFactory.createCharacteristic(params);

        final var result = transitiveChainsCharacteristic.compute(graph);
        return new SimpleCodeGraphCharacteristicResult<>(this.id, result.get()) {
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
        };
    }
}
