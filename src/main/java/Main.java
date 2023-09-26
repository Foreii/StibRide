import java.util.Objects;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import presentator.Presenter;
import view.FXMLController;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/stib.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        FXMLController fxmlController = loader.getController();
        Presenter presenter = new Presenter(fxmlController);
        presenter.initialize();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
