package presentator;

import model.Model;
import util.Observer;
import java.util.List;
import view.FXMLController;
import model.tableView.StopTableData;
import model.tableView.FavoriteTableData;
import model.exception.RepositoryException;

public class Presenter implements Observer {
    private final Model model;
    private final FXMLController view;

    public Presenter(FXMLController view) {
        this.view = view;
        this.view.register(this);
        model = new Model();
    }

    public void initialize() {
        try {
            model.initialize();
            List<String> stations = model.initializeStations();
            List<FavoriteTableData> favorites = model.initializeFavorites();
            view.initialize(stations, favorites);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addFavorite(String name, int origin, int destination) {
        try {
            FavoriteTableData favorite = model.addFavorite(name, origin, destination);
            view.displayFavorite(favorite);
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }
    public void removeFavorite(String nomTrip) {
        try {
            model.removeFavorite(nomTrip);
            view.displayRemovedFavorite(nomTrip);
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateFavorite(String nomTrip, int originKey, int destinationKey) {
        try {
            FavoriteTableData favorite = model.updateFavorite(nomTrip, originKey, destinationKey);
            view.displayUpdatedFavorite(favorite);
        } catch (RepositoryException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(int origin, int destination) {
        List<StopTableData> names = model.search(origin, destination);
        view.displaySearchedStations(names);
    }

}
