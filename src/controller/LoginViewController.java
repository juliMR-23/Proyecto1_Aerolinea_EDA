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
import principal.Aeropuerto;
import principal.Avion;
import principal.Cliente;
import principal.Piloto;
import principal.TripulanteCabina;
import principal.Vuelo;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {

    @FXML private MFXTextField     txtEmail;
    @FXML private MFXPasswordField txtPassword;
    @FXML private Label            lblError;
    @FXML private Button           btnLogin;
    @FXML private Button           btnRegistrar;
    @FXML private Button           btnVolver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    @FXML
    public void handleLogin(ActionEvent event) {
        String email    = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            error("⚠ Por favor completa todos los campos.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            error("⚠ Ingresa un correo electrónico válido.");
            return;
        }

        try {
            // TODO: Reemplazar por búsqueda real en Aerolinea cuando integres ficheros
            Object usuario = resolverUsuario(email, password);

            if (usuario == null) {
                error("⚠ Credenciales incorrectas.");
                return;
            }

            Stage   stage = (Stage) btnLogin.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            Scene   scene;

            if (usuario instanceof Piloto) {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/MainPagePilotoView.fxml")
                );
                Parent root = loader.load();
                MainPagePilotoViewController ctrl = loader.getController();
                ctrl.setPiloto((Piloto) usuario);
                scene = new Scene(root);

            } else if (usuario instanceof TripulanteCabina) {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/MainPageTripulanteCabinaView.fxml")
                );
                Parent root = loader.load();
                MainPageTripulanteCabinaViewController ctrl = loader.getController();
                ctrl.setTripulante((TripulanteCabina) usuario);
                scene = new Scene(root);

            } else if (usuario instanceof Cliente) {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/BuscarVuelosView.fxml")
                );
                Parent root = loader.load();
                BuscarVuelosViewController ctrl = loader.getController();
                ctrl.setUsuarioLogueado((Cliente) usuario);
                scene = new Scene(root);

            } else {
                error("⚠ Tipo de usuario no reconocido.");
                return;
            }

            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();

        } catch (Exception e) {
            error("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // TODO: Reemplazar por Aerolinea.buscarPersonaPorEmail(email, password) cuando integres ficheros
    private Object resolverUsuario(String email, String password) throws Exception {

        Aeropuerto aeroO = new Aeropuerto("Olaya Herrera", "Medellín",
                "Colombia", "America/Bogota", -75.4231, 6.1644);
        Aeropuerto aeroD = new Aeropuerto("El Dorado", "Bogotá",
                "Colombia", "America/Bogota", -74.1469, 4.7014);

        Avion avion = new Avion("AV-TEST", "Airbus", "A320", 180, true, 850.0);

        if (email.equals("piloto@test.com")) {
            Piloto p = new Piloto(
                    "Carlos Piloto", "CC", "1111111", "3000000000",
                    email, password,
                    8000000.0, new Date(), true, 5
            );

            TripulanteCabina[] trip = new TripulanteCabina[1];
            trip[0] = new TripulanteCabina(
                    "Ana Tripulante", "CC", "2222222", "3111111111",
                    "tripulante@test.com", password,
                    4000000.0, new Date(), true, 3
            );

            Piloto[] pilotos = new Piloto[2];
            pilotos[0] = p;
            pilotos[1] = p;

            LocalDateTime ahora = LocalDateTime.now();

            Vuelo vuelo1 = new Vuelo(
                    aeroO, aeroD,
                    ahora.plusDays(1).withHour(8).withMinute(0),
                    avion, trip, pilotos, 150000.0
            );
            Vuelo vuelo2 = new Vuelo(
                    aeroD, aeroO,
                    ahora.plusDays(3).withHour(15).withMinute(30),
                    avion, trip, pilotos, 160000.0
            );

            p.asignarVuelo(vuelo1);
            p.asignarVuelo(vuelo2);

            return p;
        }

        if (email.equals("tripulante@test.com")) {
            TripulanteCabina t = new TripulanteCabina(
                    "Ana Tripulante", "CC", "2222222", "3111111111",
                    email, password,
                    4000000.0, new Date(), true, 3
            );

            TripulanteCabina[] trip = new TripulanteCabina[1];
            trip[0] = t;

            Piloto pilotoDummy = new Piloto(
                    "Piloto Dummy", "CC", "3333333", "3222222222",
                    "dummy@piloto.com", password,
                    7000000.0, new Date(), true, 4
            );
            Piloto[] pilotos = new Piloto[2];
            pilotos[0] = pilotoDummy;
            pilotos[1] = pilotoDummy;

            LocalDateTime ahora = LocalDateTime.now();

            Vuelo vuelo1 = new Vuelo(
                    aeroO, aeroD,
                    ahora.plusDays(2).withHour(9).withMinute(15),
                    avion, trip, pilotos, 140000.0
            );
            Vuelo vuelo2 = new Vuelo(
                    aeroD, aeroO,
                    ahora.plusDays(4).withHour(18).withMinute(45),
                    avion, trip, pilotos, 155000.0
            );

            t.asignarVuelo(vuelo1);
            t.asignarVuelo(vuelo2);

            return t;
        }

        if (email.equals("cliente@test.com")) {
            return new Cliente(
                    "Miguel", "CC", "0000000", "3056540300",
                    email, password
            );
        }

        return null;
    }

    @FXML
    public void handleRegistrar(ActionEvent event) {
        try {
            Stage   stage = (Stage) btnRegistrar.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/RegisterView.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            error("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Stage   stage = (Stage) btnVolver.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/BuscarVuelosView.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            error("Error al volver: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void error(String msg) {
        lblError.setStyle("-fx-text-fill: #C0392B; -fx-font-size: 14px;");
        lblError.setText(msg);
    }
}
