package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import principal.Administrador;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageAdminViewController implements Initializable {

    @FXML private Label  lblSubtitulo;
    @FXML private Button btnCerrarSesion;

    private Administrador adminLogueado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void setAdmin(Administrador admin) {
        this.adminLogueado = admin;
        lblSubtitulo.setText("Bienvenido, " + admin.getNombre() + "  —  Administrador");
    }

    @FXML
    public void handleAeropuertos(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CRUDAeropuertosView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleAviones(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CRUDAvionesView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleVuelos(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CRUDVuelosView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleClientes(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CRUDClientesView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handlePilotos(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CRUDPilotosView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleTripulantes(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CRUDTripulantesView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        try {
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BuscarVuelosView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
