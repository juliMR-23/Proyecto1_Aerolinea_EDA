package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import principal.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML private MFXTextField     txtEmail;
    @FXML private MFXPasswordField txtPassword;
    @FXML private Label            lblError;
    @FXML private Button           btnLogin;
    @FXML private Button           btnRegistrar;
    @FXML private Button           btnVolver;

    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    @FXML
    public void handleLogin(ActionEvent event) {
        String email    = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            error("⚠ Por favor completa todos los campos.");
            return;
        }

        try {
            // Buscar en cada lista de la aerolinea por email y password
            Persona usuario = resolverUsuario(email, password);

            if (usuario == null) {
                error("⚠ Credenciales incorrectas.");
                return;
            }

            Stage   stage = (Stage) btnLogin.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader;
            Parent root;
            Scene scene;

            if (usuario instanceof Administrador) {
                loader = new FXMLLoader(getClass().getResource("/view/MainPageAdminView.fxml"));
                root   = loader.load();
                MainPageAdminViewController ctrl = loader.getController();
                ctrl.setAdmin((Administrador) usuario);
                ctrl.setAerolinea(aerolinea);

            } else if (usuario instanceof Piloto) {
                loader = new FXMLLoader(getClass().getResource("/view/MainPagePilotoView.fxml"));
                root   = loader.load();
                MainPagePilotoViewController ctrl = loader.getController();
                ctrl.setPiloto((Piloto) usuario);
                ctrl.setAerolinea(aerolinea);

            } else if (usuario instanceof TripulanteCabina) {
                loader = new FXMLLoader(getClass().getResource("/view/MainPageTripulanteCabinaView.fxml"));
                root   = loader.load();
                MainPageTripulanteCabinaViewController ctrl = loader.getController();
                ctrl.setTripulante((TripulanteCabina) usuario);
                ctrl.setAerolinea(aerolinea);

            } else if (usuario instanceof Cliente) {
                loader = new FXMLLoader(getClass().getResource("/view/BuscarVuelosView.fxml"));
                root   = loader.load();
                BuscarVuelosViewController ctrl = loader.getController();
                ctrl.setUsuarioLogueado((Cliente) usuario);
                ctrl.setAerolinea(aerolinea);

            } else {
                error("⚠ Tipo de usuario no reconocido.");
                return;
            }

            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();

        } catch (Exception e) {
            error("⚠ Error: " + e.getMessage());
        }
    }

    private Persona resolverUsuario(String email, String password) {

        // Buscar en administradores
        for (Administrador a : aerolinea.listAdministradoresActivos()) {
            if (a.getEmail().equalsIgnoreCase(email) && a.getPassword().equals(password))
                return a;
        }

        // Buscar en empleados (Piloto y TripulanteCabina)
        for (Empleado e : aerolinea.listEmpleadosActivos()) {
            if (e.getEmail().equalsIgnoreCase(email) && e.getPassword().equals(password))
                return e;
        }

        // Buscar en clientes
        for (Cliente c : aerolinea.listClientesActivos()) {
            if (c.getEmail().equalsIgnoreCase(email) && c.getPassword().equals(password))
                return c;
        }

        return null;
    }

    @FXML
    public void handleRegistrar(ActionEvent event) {
        try {
            Stage stage   = (Stage) btnRegistrar.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RegisterView.fxml"));
            Parent root = loader.load();
            RegisterViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            error("⚠ Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Stage stage   = (Stage) btnVolver.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/BuscarVuelosView.fxml"));
            Parent root = loader.load();
            BuscarVuelosViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            Scene scene = btnVolver.getScene();
            scene.setRoot(root);
            scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            error("⚠ Error al volver: " + e.getMessage());
        }
    }

    private void error(String msg) {
        lblError.setStyle("-fx-text-fill: #C0392B; -fx-font-size: 14px;");
        lblError.setText(msg);
    }
}