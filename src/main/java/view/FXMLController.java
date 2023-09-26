package view;

import util.Observer;
import java.util.List;
import util.Observable;
import javafx.fxml.FXML;
import java.util.ArrayList;
import presentator.Presenter;
import javafx.scene.input.MouseEvent;
import model.tableView.StopTableData;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import model.tableView.FavoriteTableData;
import org.controlsfx.control.SearchableComboBox;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLController implements Observable {
    private List<Observer> observers = new ArrayList<>();

    @FXML
    private TableView<StopTableData> table;

    @FXML
    private TableView<FavoriteTableData> favoriteTable;

    @FXML
    private SearchableComboBox<String> originLabel;

    @FXML
    private SearchableComboBox<String> destinationLabel;

    @FXML
    private TableColumn<StopTableData, String> stationColumn;

    @FXML
    private TableColumn<StopTableData, String> lineColumn;

    @FXML
    private TableColumn<FavoriteTableData, String> nomColumn;

    @FXML
    private TableColumn<FavoriteTableData, String> stationOne;

    @FXML
    private TableColumn<FavoriteTableData, String> stationTwo;

    @FXML
    private TextField nomLabel;

    private Presenter presenter;

    public void register(Presenter presenter) {
        this.presenter = presenter;
        addObserver(presenter);
    }

    public void initialize(List<String> stations, List<FavoriteTableData> favorites) {
        try {
            lineColumn.setCellValueFactory(new PropertyValueFactory<>("line"));
            stationColumn.setCellValueFactory(new PropertyValueFactory<>("station"));

            nomColumn.setCellValueFactory(new PropertyValueFactory<>("station"));
            stationOne.setCellValueFactory(new PropertyValueFactory<>("origin"));
            stationTwo.setCellValueFactory(new PropertyValueFactory<>("destination"));

            for (String station : stations) {
                originLabel.getItems().add(station);
                destinationLabel.getItems().add(station);
            }

            originLabel.setValue(stations.get(0));
            destinationLabel.setValue(stations.get(3));

            for (FavoriteTableData el : favorites) {
                favoriteTable.getItems().add(el);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @FXML
    public void searchButton(MouseEvent mouseEvent) {
        int originKey = originLabel.getSelectionModel().getSelectedIndex();
        int destinationKey = destinationLabel.getSelectionModel().getSelectedIndex();

        notifyObservers(originKey, destinationKey);
    }

    @FXML
    public void favoriteButton(MouseEvent mouseEvent) {
        int originKey = originLabel.getSelectionModel().getSelectedIndex();
        int destinationKey = destinationLabel.getSelectionModel().getSelectedIndex();
        String nomTrip = nomLabel.getText();

        presenter.addFavorite(nomTrip, originKey, destinationKey);
    }

    @FXML
    public void removeButton(MouseEvent mouseEvent) {
        String nomTrip = nomLabel.getText();
        presenter.removeFavorite(nomTrip);
    }

    @FXML
    public void modifyButton(MouseEvent mouseEvent) {
        String nomTrip = nomLabel.getText();
        int originKey = originLabel.getSelectionModel().getSelectedIndex();
        int destinationKey = destinationLabel.getSelectionModel().getSelectedIndex();

        presenter.updateFavorite(nomTrip, originKey, destinationKey);
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    public void displaySearchedStations(List<StopTableData> names) {
        table.getItems().clear();
        for (StopTableData el : names) {
            table.getItems().add(el);
        }
    }

    public void displayFavorite(FavoriteTableData favorite) {
        if (favorite != null) {
            favoriteTable.getItems().add(favorite);
        }
    }

    public void displayRemovedFavorite(String nomTrip) {
        for (FavoriteTableData el : favoriteTable.getItems()) {
            if (el.getStation().equals(nomTrip)) {
                favoriteTable.getItems().remove(el);
                break;
            }
        }
    }

    @Override
    public void notifyObservers(int originKey, int destinationKey) {
        for (Observer observer : observers) {
            observer.update(originKey, destinationKey);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void displayUpdatedFavorite(FavoriteTableData favorite) {
        if (favorite != null) {
            for (FavoriteTableData el : favoriteTable.getItems()) {
                if (el.getStation().equals(favorite.getStation())) {
                    favoriteTable.getItems().remove(el);
                    break;
                }
            }

            favoriteTable.getItems().add(favorite);
        }
    }
}
