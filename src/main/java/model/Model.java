package model;

import java.util.Set;
import java.util.List;
import model.dto.StopDto;
import model.graph.Station;
import java.util.ArrayList;
import java.io.IOException;
import model.dto.StationDto;
import model.dto.FavoriteDto;
import model.graph.GraphMetro;
import model.config.ConfigManager;
import model.tableView.StopTableData;
import model.repository.StopRepository;
import model.tableView.FavoriteTableData;
import model.repository.StationRepository;
import model.exception.RepositoryException;
import model.repository.FavoriteRepository;

import static model.graph.Dijkstra.calculateShortestPathFromSource;

public class Model {
    private GraphMetro graphMetro;

    public Model() {
        this.graphMetro = new GraphMetro();
    }

    public void createGraphMetro() {
        try {
            StationRepository stationRepository = new StationRepository();
            List<StationDto> stationDto = stationRepository.getAll();

            Station st;

            for (StationDto dto : stationDto) {
                st = new Station(dto);
                graphMetro.addNode(st);
            }

            StopRepository stopRepository = new StopRepository();
            List<StopDto> stopDto = stopRepository.getAll();

            for (int i = 0; i < stopDto.size(); i++) {
                int lineId = stopDto.get(i).getKey().getFirst();
                StationDto sta = stationRepository.get(stopDto.get(i).getKey().getSecond());

                if (i > 0 && lineId == stopDto.get(i - 1).getKey().getFirst()) {
                    graphMetro.addDestination(graphMetro.getStation(sta), graphMetro.getStation(stationRepository.get(stopDto.get(i - 1).getKey().getSecond())));
                }

                if (i + 1 < stopDto.size() && lineId == stopDto.get(i + 1).getKey().getFirst()) {
                    graphMetro.addDestination(graphMetro.getStation(sta), graphMetro.getStation(stationRepository.get(stopDto.get(i + 1).getKey().getSecond())));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public GraphMetro getGraphMetro() {
        return graphMetro;
    }

    public void updateShortestPath(Station source) {
        graphMetro = calculateShortestPathFromSource(graphMetro, source);
    }

    public List<StopTableData> search(int origin, int destination) {
        try {
            StationRepository stationRepository = new StationRepository();
            List<StationDto> stationList = stationRepository.getAll();
            StationDto stationOrigin = stationList.get(origin);
            StationDto stationDestination = stationList.get(destination);


            List<StopTableData> li = new ArrayList<>();
            createGraphMetro();

            updateShortestPath(getGraphMetro().getStation(stationOrigin));
            Set<Station> stations = getGraphMetro().getNodes();

            for (Station station : stations) {
                if (station.equals(getGraphMetro().getStation(stationDestination))) {
                    for (Station s : station.getShortestPath()) {
                        String station_name = s.getName().getName();
                        int station_key = s.getName().getKey();

                        StopRepository stopRepository = new StopRepository();
                        List<StopDto> list = stopRepository.getAllByStationId(station_key);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (StopDto pp : list) {
                            int line = pp.getKey().getFirst();
                            stringBuilder.append(line).append(" ");
                        }
                        String result = stringBuilder.toString();
                        StopTableData stop = new StopTableData(station_name, result);
                        li.add(stop);
                    }
                    break;
                }
            }
            return li;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public FavoriteTableData addFavorite(String name, int origin, int destination) throws RepositoryException {
        StationRepository stationRepository = new StationRepository();
        List<StationDto> stationList = stationRepository.getAll();
        int originKey = stationList.get(origin).getKey();
        int destinationKey = stationList.get(destination).getKey();

        FavoriteRepository repository = new FavoriteRepository();

        if (!repository.contains(name)) {
            FavoriteDto fav = new FavoriteDto(name, originKey, destinationKey);
            repository.add(fav);

            String originName = stationRepository.get(originKey).getName();
            String destinationName = stationRepository.get(destinationKey).getName();

            return new FavoriteTableData(fav.getKey(), originName, destinationName);
        }
        return null;
    }

    public void removeFavorite(String nomTrip) throws RepositoryException {
        FavoriteRepository repository = new FavoriteRepository();
        repository.remove(nomTrip);
    }

    public FavoriteTableData updateFavorite(String nomTrip, int origin, int destination) throws RepositoryException {
        FavoriteRepository repository = new FavoriteRepository();
        if (repository.contains(nomTrip)) {
            StationRepository stationRepository = new StationRepository();
            List<StationDto> stationList = stationRepository.getAll();

            int originKey = stationList.get(origin).getKey();
            int destinationKey = stationList.get(destination).getKey();

            repository.update(new FavoriteDto(nomTrip, originKey, destinationKey));

            String originName = stationRepository.get(originKey).getName();
            String destinationName = stationRepository.get(destinationKey).getName();

            return new FavoriteTableData(nomTrip, originName, destinationName);
        }
        return null;
    }

    public void initialize() throws IOException {
        ConfigManager.getInstance().load();
        ConfigManager.getInstance().getProperties("db.url");
    }

    public List<String> initializeStations() throws RepositoryException {
        List<String> stations = new ArrayList<>();

        StationRepository repository = new StationRepository();
        for (StationDto el : repository.getAll()) {
            stations.add(el.getName());
        }
        return stations;
    }

    public List<FavoriteTableData> initializeFavorites() throws RepositoryException {
        List<FavoriteTableData> list = new ArrayList<>();

        FavoriteRepository favoriteRepository = new FavoriteRepository();
        List<FavoriteDto> li = favoriteRepository.getAll();
        StationRepository stationRepository = new StationRepository();

        for (FavoriteDto el : li) {
            list.add(new FavoriteTableData(el.getKey(), stationRepository.get(el.getId_origin()).getName(), stationRepository.get(el.getId_destination()).getName()));
        }
        return list;
    }

}
