package ru.joke.cdgraph.core.impl.characteristics.clusters;

import ru.joke.cdgraph.core.*;
import ru.joke.cdgraph.core.impl.characteristics.SimpleCodeGraphCharacteristicResult;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

final class ModuleClusteringCharacteristic implements CodeGraphCharacteristic<Collection<Cluster>> {

    private final String id;
    private final ModuleClusteringCharacteristicParameters parameters;

    ModuleClusteringCharacteristic(@Nonnull String id, @Nonnull ModuleClusteringCharacteristicParameters parameters) {
        this.parameters = parameters;
        this.id = id;
    }

    @Nonnull
    @Override
    public CodeGraphCharacteristicResult<Collection<Cluster>> compute(@Nonnull CodeGraph graph) {
        final Set<Cluster> clusters = createInitialClusters(graph);

        Map<Cluster, Set<Cluster>> adjMap = createAdjMap(clusters);
        final int totalEdges = calculateTotalEdges(adjMap);

        int round = 0;
        while (clusters.size() > this.parameters.optimizingFactor() && ++round <= this.parameters.maxRounds()) {

            final var currentRoundInitialClusters = new HashSet<>(clusters);
            for (Cluster sourceCluster : currentRoundInitialClusters) {
                double maxModularity = Double.NEGATIVE_INFINITY;
                Cluster bestCluster = sourceCluster;

                final Set<Cluster> targetClusters =
                        sourceCluster.relations()
                                        .stream()
                                        .map(ClusterRelation::target)
                                        .collect(Collectors.toSet());
                for (Cluster targetCluster : targetClusters) {

                    final double modularity = calculateModularity(totalEdges, adjMap.keySet(), sourceCluster, targetCluster);
                    if (modularity > maxModularity) {
                        maxModularity = modularity;
                        bestCluster = targetCluster;
                    }
                }

                if (maxModularity > 0) {
                    joinClusters(sourceCluster, bestCluster, clusters);
                    adjMap = createAdjMap(clusters);

                    break;
                }
            }

            if (currentRoundInitialClusters.equals(clusters)) {
                break;
            }
        }

        final var resultClustersList = List.copyOf(clusters);
        return new SimpleCodeGraphCharacteristicResult<>(this.id, this.parameters, resultClustersList) {
            @Override
            public String toJson() {
                final var clustersMaps = createClusterDataMaps(resultClustersList);
                return toJson(clustersMaps);
            }
        };
    }

    private Set<Map<String, Object>> createClusterDataMaps(final List<Cluster> clusters) {
        return clusters
                .stream()
                .map(cluster -> createClusterDataMap(cluster, clusters))
                .collect(Collectors.toSet());
    }

    private Map<String, Object> createClusterDataMap(final Cluster cluster, final List<Cluster> clustersList) {

        final Set<String> includedNodes =
                cluster.includedNodes()
                        .stream()
                        .map(GraphNode::id)
                        .collect(Collectors.toSet());
        final int[] adjClustersIds =
                cluster.relations()
                        .stream()
                        .map(ClusterRelation::target)
                        .mapToInt(clustersList::indexOf)
                        .toArray();
        return Map.of("includedModules", includedNodes, "relatedWith", adjClustersIds);
    }

    private void joinClusters(
            final Cluster sourceCluster,
            final Cluster targetCluster,
            final Collection<Cluster> clusters) {

        final Set<GraphNode> includedNodesInNewCluster = new HashSet<>(sourceCluster.includedNodes());
        includedNodesInNewCluster.addAll(targetCluster.includedNodes());

        final Cluster newCluster = new Cluster(includedNodesInNewCluster, new HashSet<>());

        sourceCluster.relations()
                        .stream()
                        .map(ClusterRelation::target)
                        .filter(relatedCluster -> relatedCluster != targetCluster)
                        .map(relatedCluster -> new ClusterRelation(newCluster, relatedCluster))
                        .forEach(newCluster.relations()::add);

        targetCluster.relations()
                .stream()
                .map(ClusterRelation::target)
                .map(relatedCluster -> new ClusterRelation(newCluster, relatedCluster))
                .forEach(newCluster.relations()::add);

        clusters.remove(targetCluster);
        clusters.remove(sourceCluster);

        for (Cluster cluster : clusters) {
            final Set<ClusterRelation> newRelations = new HashSet<>();
            for (ClusterRelation relation : cluster.relations()) {
                if (relation.target() == sourceCluster || relation.target() == targetCluster) {
                    newRelations.add(new ClusterRelation(relation.source(), newCluster));
                } else {
                    newRelations.add(relation);
                }
            }

            cluster.relations().clear();
            cluster.relations().addAll(newRelations);
        }

        clusters.add(newCluster);
    }

    private Set<Cluster> createInitialClusters(final CodeGraph graph) {
        final Map<Integer, Cluster> clusters = new HashMap<>();
        final Map<GraphNode, Integer> nodesMap = new HashMap<>();
        final var nodes = graph.findAllNodes();
        final var iterator = nodes.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            final GraphNode node = iterator.next();
            final var cluster = new Cluster(Set.of(node), new HashSet<>(node.relations().size()));

            clusters.put(i, cluster);
            nodesMap.put(node, i);
        }

        clusters.forEach((id, cluster) -> {
            final var clusterRelations =
                    cluster.includedNodes()
                            .stream()
                            .map(GraphNode::relations)
                            .flatMap(Set::stream)
                            .map(GraphNodeRelation::target)
                            .map(nodesMap::get)
                            .map(clusters::get)
                            .map(targetCluster -> new ClusterRelation(cluster, targetCluster))
                            .collect(Collectors.toSet());
            cluster.relations().addAll(clusterRelations);
        });

        return new HashSet<>(clusters.values());
    }

    private double calculateModularity(
            final int totalEdges,
            final Set<Cluster> clusters,
            final Cluster targetCluster,
            final Cluster sourceCluster) {
        return computeOneClusterModularity(sourceCluster, clusters, totalEdges)
                - computeOneClusterModularity(targetCluster, clusters, totalEdges);
    }

    private double computeOneClusterModularity(final Cluster cluster, final Set<Cluster> clusters, final int totalEdges) {

        final int totalExternalInputEdgesCount = cluster.computeExternalInputEdgesCount(clusters);
        final int totalOutputEdgesCount = cluster.computeOutputEdgesCount();

        return (double) totalExternalInputEdgesCount * totalOutputEdgesCount / (2 * totalEdges)
                + (double) totalExternalInputEdgesCount * totalOutputEdgesCount / (4 * totalEdges ^ 2);
    }

    private int calculateTotalEdges(final Map<Cluster, Set<Cluster>> adjMatrix) {
        return adjMatrix.values()
                        .stream()
                        .mapToInt(Collection::size)
                        .sum();
    }

    private Map<Cluster, Set<Cluster>> createAdjMap(final Collection<Cluster> clusters) {
        final Map<Cluster, Set<Cluster>> adjMap = new HashMap<>(clusters.size() + 1, 1);

        for (final Cluster cluster : clusters) {

            for (final ClusterRelation relation : cluster.relations()) {
                final Cluster targetCluster = relation.target();
                adjMap.computeIfAbsent(cluster, k -> new HashSet<>()).add(targetCluster);
            }
        }

        return adjMap;
    }
}
