package model.graph;

import java.util.*;

public class Dijkstra {
    public static GraphMetro calculateShortestPathFromSource(GraphMetro graph, Station source) {
        source.setDistance(0);

        Set<Station> settledNodes = new HashSet<>();
        Set<Station> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Station currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< Station, Integer> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                Station adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static Station getLowestDistanceNode(Set < Station > unsettledNodes) {
        Station lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Station node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Station evaluationNode,
                                                 Integer edgeWeigh, Station sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Station> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

}
