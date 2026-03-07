package controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class LoginViewController {
	
    @FXML private MFXTextField txtUsuario;  
    @FXML private MFXPasswordField passSesion;  
    @FXML private MFXButton btnIniciaSesion;
    @FXML private MFXButton btnRegistrate;
    @FXML private MFXButton btnVolver;
    
    @FXML
    private void handleLogin(ActionEvent event) {
    	String user = txtUsuario.getText();
    	String pass = passSesion.getText();
    	if (user.isEmpty()) {
    		new Alert(Alert.AlertType.WARNING, "Faltan campos por llenar").show();
    		return;
    	}
    	
    	// TODO : Logica Login
    	
    	System.out.println("Inicio sesion" + user);
    }
    
    @FXML
    private void handleRegistrar(ActionEvent event) {
    	loadRegisterView();
    }
    
    
}
