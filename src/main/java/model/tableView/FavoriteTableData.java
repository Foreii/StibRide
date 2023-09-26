package model.tableView;

public class FavoriteTableData {
    private final String station;
    private final String origin;
    private final String destination;

    public String getStation() {
        return station;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public FavoriteTableData(String station, String origin, String destination) {
        this.station = station;
        this.origin = origin;
        this.destination = destination;
    }
}
