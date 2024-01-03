package ru.joke.cdgraph.core.impl.characteristics.factors;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.impl.characteristics.SingleModuleCharacteristicParameters;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;

import static ru.joke.cdgraph.core.impl.AbstractCodeGraph.CLASSES_METADATA_TAG;
import static ru.joke.cdgraph.core.impl.AbstractCodeGraph.SOURCE_MODULE_TAG;

/**
 * A characteristic that computes module abstractness (metric from the book 'Clean Architecture').
 * The abstractness of the module is calculated as the ratio between the number of abstract elements
 * (interfaces, annotations, and abstract classes) and the total number of elements in the module.<br>
 * Type of the characteristic parameters: {@link SingleModuleCharacteristicParameters}.
 *
 * @author Alik
 *
 * @see AbstractnessCharacteristicFactory
 * @see AbstractnessCharacteristicFactoryHandle
 */
final class AbstractnessCharacteristic implements CodeGraphCharacteristic<Factor> {

    private final String id;
    private final SingleModuleCharacteristicParameters parameters;

    AbstractnessCharacteristic(
            @Nonnull String id,
            @Nonnull SingleModuleCharacteristicParameters parameters) {
        this.parameters = parameters;
        this.id = id;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Factor> compute(@Nonnull CodeGraph graph) {
        final var targetNode = graph.findNodeById(this.parameters.moduleId())
                                    .orElseThrow(() -> new CodeGraphComputationException("Node '" + this.parameters.moduleId() + "' not found"));

        @SuppressWarnings("unchecked")
        final var isSourceTag = (GraphTag<Boolean>) targetNode.tags().get(SOURCE_MODULE_TAG);
        if (isSourceTag == null || !isSourceTag.value()) {
            throw new CodeGraphComputationException("Abstractness is non-computable for a non-source module: " + targetNode.id());
        }

        @SuppressWarnings("unchecked")
        final var metadataTag = (GraphTag<Set<ClassMetadata>>) targetNode.tags().get(CLASSES_METADATA_TAG);
        final Set<ClassMetadata> classesMetadata =
                metadataTag == null || metadataTag.value().isEmpty()
                        ? Collections.emptySet()
                        : metadataTag.value();

        final long abstractElements = classesMetadata
                                            .stream()
                                            .map(ClassMetadata::classType)
                                            .filter(Predicate.not(ClassMetadata.ClassType::isConcrete))
                                            .count();
        final double abstractness = classesMetadata.isEmpty() ? 1.0 : (double) abstractElements / classesMetadata.size();
        return new SimpleCodeGraphCharacteristicResult<>(this.id, this.parameters, new Factor(abstractness));
    }
}