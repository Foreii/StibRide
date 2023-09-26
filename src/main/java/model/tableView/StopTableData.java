package model.tableView;

public class StopTableData {
    private final String station;
    private final String line;

    public String getLine() {
        return line;
    }

    public String getStation() {
        return station;
    }

    public StopTableData(String station, String line) {
        this.station = station;
        this.line = line;
    }

}
