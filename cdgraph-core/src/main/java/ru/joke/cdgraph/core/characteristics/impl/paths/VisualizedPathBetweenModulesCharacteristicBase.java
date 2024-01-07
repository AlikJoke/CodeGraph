package ru.joke.cdgraph.core.characteristics.impl.paths;

import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristic;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.graph.GraphTag;
import ru.joke.cdgraph.core.graph.impl.SimpleGraphTag;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

abstract class VisualizedPathBetweenModulesCharacteristicBase<T> implements CodeGraphCharacteristic<T> {

    private static final String START_TAG = "start";
    private static final String END_TAG = "end";
    private static final String PATH_INDEX_TAG = "path-index";

    protected void addTagsToPathComponentsInGraphCopy(
            @Nonnull final CodeGraph graphCopy,
            @Nonnull final PathBetweenModules path,
            @Nonnegative final int pathIndex) {

        final var startNode = path.modulesInPath().getFirst();
        final var endNode = path.modulesInPath().getLast();

        graphCopy.findNodeById(startNode.id())
                    .ifPresent(node -> addNodeTagIfNeed(node, START_TAG));
        graphCopy.findNodeById(endNode.id())
                    .ifPresent(node -> addNodeTagIfNeed(node, END_TAG));

        for (int i = 0; i < path.relationsInPath().size(); i++) {
            final var relation = path.relationsInPath().get(i);
            addRelationPathIndexTag(graphCopy, relation, pathIndex + "." + i);
        }
    }

    private void addRelationPathIndexTag(
            final CodeGraph graphCopy,
            final GraphNodeRelation relation,
            final String index) {

        graphCopy.findNodeById(relation.source().id())
                    .stream()
                    .map(GraphNode::relations)
                    .flatMap(Set::stream)
                    .filter(relationCopy -> relationCopy.target().equals(relation.target()))
                    .forEach(relationCopy -> addRelationTagIfNeed(relationCopy, index));
    }

    private void addRelationTagIfNeed(final GraphNodeRelation relation, final String index) {
        @SuppressWarnings("unchecked")
        final GraphTag<List<String>> tag =
                (GraphTag<List<String>>) relation.tags().computeIfAbsent(
                        PATH_INDEX_TAG,
                        tagId -> new SimpleGraphTag<>(tagId, new ArrayList<>())
                );
        tag.value().add(index);
    }

    private void addNodeTagIfNeed(final GraphNode node, final String tagName) {
        node.tags().computeIfAbsent(tagName, tag -> new SimpleGraphTag<>(tag, true));
    }
}
