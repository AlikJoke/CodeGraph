import ru.joke.cdgraph.core.characteristics.CodeGraphCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.bridges.DependencyBridgesCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.clusters.ModuleClusteringCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.counters.DependenciesCountCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.factors.AbstractnessCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.factors.AllModulesAbstractnessCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.factors.AllModulesStabilityCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.factors.StabilityCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.locations.AllClassesDuplicatesLocationsCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.locations.ClassLocationsCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.locations.ConflictingDependenciesCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.locations.PackageLocationsCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.paths.AllPathsBetweenModulesCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.paths.CodeGraphDescriptionCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.paths.ShortestPathBetweenModulesCharacteristicFactory;
import ru.joke.cdgraph.core.characteristics.impl.paths.TransitiveChainsCharacteristicFactory;

/**
 * Core CodeGraph Module which contains main API and its default implementation.
 *
 * @author Alik
 */
module ru.joke.cdgraph.core {
    exports ru.joke.cdgraph.core.graph;
    exports ru.joke.cdgraph.core.graph.impl.maven;
    exports ru.joke.cdgraph.core.graph.impl.jpms;

    exports ru.joke.cdgraph.core.meta;
    exports ru.joke.cdgraph.core.meta.impl;

    exports ru.joke.cdgraph.core.datasources;
    exports ru.joke.cdgraph.core.datasources.impl;

    exports ru.joke.cdgraph.core.client;
    exports ru.joke.cdgraph.core.client.impl;

    exports ru.joke.cdgraph.core.characteristics;
    exports ru.joke.cdgraph.core.characteristics.impl;
    exports ru.joke.cdgraph.core.characteristics.impl.clusters;
    exports ru.joke.cdgraph.core.characteristics.impl.paths;
    exports ru.joke.cdgraph.core.characteristics.impl.counters;
    exports ru.joke.cdgraph.core.characteristics.impl.factors;
    exports ru.joke.cdgraph.core.characteristics.impl.locations;
    exports ru.joke.cdgraph.core.characteristics.impl.bridges;

    uses CodeGraphCharacteristicFactory;

    provides CodeGraphCharacteristicFactory with
            DependencyBridgesCharacteristicFactory,
            ModuleClusteringCharacteristicFactory,
            DependenciesCountCharacteristicFactory,
            AbstractnessCharacteristicFactory,
            AllModulesAbstractnessCharacteristicFactory,
            StabilityCharacteristicFactory,
            AllModulesStabilityCharacteristicFactory,
            AllClassesDuplicatesLocationsCharacteristicFactory,
            ClassLocationsCharacteristicFactory,
            ConflictingDependenciesCharacteristicFactory,
            PackageLocationsCharacteristicFactory,
            AllPathsBetweenModulesCharacteristicFactory,
            ShortestPathBetweenModulesCharacteristicFactory,
            TransitiveChainsCharacteristicFactory,
            CodeGraphDescriptionCharacteristicFactory;

    requires com.google.gson;

    requires jsr305;

    requires maven.model;

    requires java.xml;
    requires java.net.http;
}