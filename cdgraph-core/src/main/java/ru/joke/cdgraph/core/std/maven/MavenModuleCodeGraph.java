package ru.joke.cdgraph.core.std.maven;

import ru.joke.cdgraph.core.CodeGraphDataSource;
import ru.joke.cdgraph.core.GraphNode;
import ru.joke.cdgraph.core.std.AbstractCodeGraph;

import javax.annotation.Nonnull;
import java.util.Map;

public final class MavenModuleCodeGraph extends AbstractCodeGraph {

    public MavenModuleCodeGraph(@Nonnull CodeGraphDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected Map<String, GraphNode> buildNodesMap(@Nonnull CodeGraphDataSource dataSource) {
        // TODO
        return null;
    }
}
