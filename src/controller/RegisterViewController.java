package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import principal.Aerolinea;
import excepciones.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXPasswordField;

public class RegisterViewController {

    @FXML private MFXTextField    txtNombre;
    @FXML private MFXTextField    txtTipoDocumento;
    @FXML private MFXTextField    txtDocumento;
    @FXML private MFXTextField    txtTelefono;
    @FXML private MFXTextField    txtEmail;
    @FXML private MFXPasswordField txtPassword;
    @FXML private Label           lblMensaje;
    @FXML private MFXButton       btnRegistrar;
    @FXML private MFXButton       btnVolver;

    private Aerolinea aerolinea;

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    @FXML
    public void handleRegistrar(ActionEvent event) {
        String nombre    = txtNombre.getText().trim();
        String tipoDoc   = txtTipoDocumento.getText().trim();
        String documento = txtDocumento.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String email     = txtEmail.getText().trim();
        String password  = txtPassword.getText().trim();

        try {
            aerolinea.addCliente(nombre, tipoDoc, documento, telefono, email, password);
            aerolinea.guardarClientes();

            lblMensaje.setStyle("-fx-text-fill: #27AE60;");
            lblMensaje.setText("✔ Cuenta creada exitosamente. ¡Ahora puedes iniciar sesión!");
            btnRegistrar.setDisable(true);

        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: #C0392B;");
            lblMensaje.setText("⚠ " + e.getMessage());
        }
    }

    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Stage stage   = (Stage) btnVolver.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/LoginView.fxml")
            );
            Parent root = loader.load();
            LoginViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            lblMensaje.setStyle("-fx-text-fill: #C0392B;");
            lblMensaje.setText("⚠ Error al volver: " + e.getMessage());
        }
    }
}