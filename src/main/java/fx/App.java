package fx;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static BorderPane mainlayout;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Gestion de Contacts");

        // Chargement du layout principal
        mainlayout = loadFXML("MainLayout");
        scene = new Scene(mainlayout, 800, 600);
        stage.setScene(scene);
        stage.show();

        // Charger automatiquement la vue des contacts
        setRoot("PersonView");
    }

    public static void setRoot(String fxml) throws IOException {
        mainlayout.setCenter(loadFXML(fxml));
    }

    private static BorderPane loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/db/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
