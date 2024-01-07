package ru.joke.cdgraph.starter.console.visualizer;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicResult;
import ru.joke.cdgraph.core.graph.CodeGraph;
import ru.joke.cdgraph.core.graph.GraphNode;
import ru.joke.cdgraph.core.graph.GraphNodeRelation;
import ru.joke.cdgraph.core.graph.GraphTag;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Visualizer of the characteristics results based on the GraphStream UI Library.
 *
 * @author Alik
 * @see CodeGraphCharacteristicResult
 * @see Graph
 */
public final class CodeGraphCharacteristicResultsVisualizer {

    private static final String UI_QUALITY_ATTR = "ui.quality";
    private static final String UI_ANTI_ALIAS_ATTR = "ui.antialias";
    private static final String UI_TITLE_ATTR = "ui.title";
    private static final String UI_LABEL = "ui.label";
    private static final String UI_STYLE = "ui.style";

    private static final String UI_PACKAGE = "org.graphstream.ui";
    private static final String UI_SWING_PACKAGE = "swing";

    private final List<CodeGraphCharacteristicResult<?>> characteristicResults;

    static {
        System.setProperty(UI_PACKAGE, UI_SWING_PACKAGE);
    }

    public CodeGraphCharacteristicResultsVisualizer(@Nonnull List<CodeGraphCharacteristicResult<?>> characteristicResults) {
        this.characteristicResults = characteristicResults;
    }

    public void visualize() {
        this.characteristicResults.forEach(this::visualize);
    }

    private void visualize(@Nonnull CodeGraphCharacteristicResult<?> characteristicResult) {

        final var gsGraph = buildGraphStreamGraph(characteristicResult.characteristicId(), characteristicResult.visualizedGraph());

        final var viewer = gsGraph.display(true);
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
    }

    private Graph buildGraphStreamGraph(final String characteristicId, final CodeGraph visualizedGraph) {

        final var gsGraph = new SingleGraph(characteristicId, true, true);

        visualizedGraph.findAllNodes()
                        .forEach(node -> processNode(gsGraph, node));

        gsGraph.setAttribute(UI_QUALITY_ATTR);
        gsGraph.setAttribute(UI_ANTI_ALIAS_ATTR);
        gsGraph.setAttribute(UI_TITLE_ATTR, characteristicId);

        return gsGraph;
    }

    private void processNode(final Graph gsGraph, final GraphNode node) {

        final var sourceNode = addGsNode(gsGraph, node);
        node.relations()
                .forEach(relation -> addGsRelation(gsGraph, sourceNode, relation));
    }

    private void addGsRelation(
            final Graph gsGraph,
            final Node sourceNode,
            final GraphNodeRelation relation) {
        final var targetNode = addGsNode(gsGraph, relation.target());
        final var relationId = sourceNode.getId() + "_" + targetNode.getId();

        final var edge = gsGraph.addEdge(relationId, sourceNode, targetNode, true);

        edge.setAttribute(UI_STYLE, "text-alignment:center;arrow-size:5;");
        edge.setAttribute(UI_LABEL, createTagsLabel(relation.tags()));
    }

    private Node addGsNode(final Graph graph, final GraphNode node) {
        final Node existingGsNode = graph.getNode(node.id());
        if (existingGsNode != null) {
            return existingGsNode;
        }

        final var addedNode = graph.addNode(node.id());

        addedNode.setAttribute(UI_STYLE, "shape:circle;fill-color:red;size:20px;text-alignment:at-right;text-size:15;text-padding:2;size-mode:fit;");
        addedNode.setAttribute(UI_LABEL, addedNode.getId() + createTagsLabel(node.tags()));

        return addedNode;
    }

    private String createTagsLabel(final Map<String, GraphTag<?>> tagsMap) {

        if (tagsMap.isEmpty()) {
            return "";
        }

        final var tags = tagsMap.values();
        return tags
                .stream()
                .map(tag -> tag.name() + ":" + tag.value())
                .collect(Collectors.joining(",", " {", "}"));
    }
}
