package ru.joke.cdgraph.core.graph.impl;

import ru.joke.cdgraph.core.graph.CodeGraph;

import java.util.Collections;

import static ru.joke.cdgraph.core.test.util.TestUtil.makeGraphCloningChecks;

public abstract class AbstractCodeGraphTestBase {

    protected void makeCloningChecks(CodeGraph sourceGraph) {
        makeGraphCloningChecks(sourceGraph, Collections.emptySet());
        makeGraphCloningChecks(sourceGraph, Collections.emptySet(), CodeGraph.CloneOptions.CLEAR_TAGS);
        makeGraphCloningChecks(sourceGraph, Collections.emptySet(), CodeGraph.CloneOptions.CLEAR_CLASSES_METADATA);
    }
}
