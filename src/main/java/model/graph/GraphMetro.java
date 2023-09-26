package model.graph;

import model.dto.StationDto;

import java.util.HashSet;
import java.util.Set;

public class GraphMetro {
    private Set<Station> nodes = new HashSet<>();

    public void addNode(Station nodeA) {
        nodes.add(nodeA);
    }

    public Set<Station> getNodes() {
        return nodes;
    }

    public void addDestination(Station targetStation, Station destination) {
        for (Station station : nodes) {
            if (station.equals(targetStation)) {
                station.addDestination(destination, 1);
            }
        }
    }

    public Station getStation(StationDto st) {
        for (Station station : nodes) {
            if (station.getName().equals(st)) {
                return station;
            }
        }
        return null;
    }
}
