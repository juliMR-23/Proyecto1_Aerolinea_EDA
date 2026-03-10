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
import principal.Aerolinea;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageAdminViewController implements Initializable {

    @FXML private Label  lblSubtitulo;
    @FXML private Button btnCerrarSesion;

    private Administrador adminLogueado;
    private Aerolinea     aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public void setAdmin(Administrador admin) {
        this.adminLogueado = admin;
        lblSubtitulo.setText("Bienvenido, " + admin.getNombre() + "  —  Administrador");
    }

    private void navegar(ActionEvent event, String fxml) {
        try {
            Stage stage   = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Object ctrl = loader.getController();
            if (ctrl instanceof CRUDAeropuertosViewController)
                ((CRUDAeropuertosViewController) ctrl).setAerolinea(aerolinea);
            else if (ctrl instanceof CRUDAvionesViewController)
                ((CRUDAvionesViewController) ctrl).setAerolinea(aerolinea);
            else if (ctrl instanceof CRUDVuelosViewController)
                ((CRUDVuelosViewController) ctrl).setAerolinea(aerolinea);
            else if (ctrl instanceof CRUDClientesViewController)
                ((CRUDClientesViewController) ctrl).setAerolinea(aerolinea);
            else if (ctrl instanceof CRUDPilotosViewController)
                ((CRUDPilotosViewController) ctrl).setAerolinea(aerolinea);
            else if (ctrl instanceof CRUDTripulantesViewController)
                ((CRUDTripulantesViewController) ctrl).setAerolinea(aerolinea);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            lblSubtitulo.setText("⚠ Error al navegar: " + e.getMessage());
            lblSubtitulo.setStyle("-fx-text-fill: #C0392B;");
        }
    }

    @FXML
    public void handleAeropuertos(ActionEvent event) {
        navegar(event, "/view/CRUDAeropuertosView.fxml");
    }

    @FXML
    public void handleAviones(ActionEvent event) {
        navegar(event, "/view/CRUDAvionesView.fxml");
    }

    @FXML
    public void handleVuelos(ActionEvent event) {
        navegar(event, "/view/CRUDVuelosView.fxml");
    }

    @FXML
    public void handleClientes(ActionEvent event) {
        navegar(event, "/view/CRUDClientesView.fxml");
    }

    @FXML
    public void handlePilotos(ActionEvent event) {
        navegar(event, "/view/CRUDPilotosView.fxml");
    }

    @FXML
    public void handleTripulantes(ActionEvent event) {
        navegar(event, "/view/CRUDTripulantesView.fxml");
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        try {
            Stage stage   = (Stage) btnCerrarSesion.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BuscarVuelosView.fxml"));
            Parent root = loader.load();
            BuscarVuelosViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            Scene scene = btnCerrarSesion.getScene();
            scene.setRoot(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            lblSubtitulo.setText("⚠ Error al cerrar sesión: " + e.getMessage());
            lblSubtitulo.setStyle("-fx-text-fill: #C0392B;");
        }
    }
}