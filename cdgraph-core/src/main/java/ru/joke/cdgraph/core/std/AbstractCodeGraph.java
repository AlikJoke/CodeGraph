package ru.joke.cdgraph.core.std;

import ru.joke.cdgraph.core.CodeGraph;
import ru.joke.cdgraph.core.CodeGraphConfigurationException;
import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.GraphNode;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractCodeGraph implements CodeGraph {

    protected final GraphNode rootNode;

    protected AbstractCodeGraph(@Nonnull CodeGraphDataSource dataSource) {
        final Map<String, GraphNode> nodesMap = buildNodesMap(dataSource);

        final Set<String> allNodeIds = new HashSet<>(nodesMap.keySet());
        nodesMap.values()
                    .forEach(node -> removeDependentNodesFromMap(node, allNodeIds));

        if (allNodeIds.size() != 1) {
            throw new CodeGraphConfigurationException("Found more than 1 root module: " + allNodeIds.size());
        }

        this.rootNode = nodesMap.get(allNodeIds.iterator().next());
    }

    @Nonnull
    @Override
    public GraphNode rootNode() {
        return this.rootNode;
    }

    protected abstract Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource);

    private void removeDependentNodesFromMap(final GraphNode node, final Set<String> allNodesIds) {
        node.dependencies()
                .forEach(dependency -> allNodesIds.remove(dependency.target().id()));
    }
}
