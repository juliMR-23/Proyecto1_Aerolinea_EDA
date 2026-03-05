package principal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override public void start(Stage stage) {
        VBox root = new VBox(new Label("¡Aerolínea EDA JavaFX!"));
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }
    public static void main(String[] args) { launch(); }
}
