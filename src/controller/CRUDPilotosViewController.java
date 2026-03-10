package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import principal.Aerolinea;
import principal.Piloto;

import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class CRUDPilotosViewController implements Initializable {

    @FXML private TableView<Piloto>          tblPilotos;
    @FXML private TableColumn<Piloto,String> colId;
    @FXML private TableColumn<Piloto,String> colNombre;
    @FXML private TableColumn<Piloto,String> colDocumento;
    @FXML private TableColumn<Piloto,String> colEmail;
    @FXML private TableColumn<Piloto,Double> colSalario;
    @FXML private TableColumn<Piloto,Double> colHorasAcumuladas;
    @FXML private TableColumn<Piloto,Void>   colAcciones;
    @FXML private Label                      lblEstado;
    @FXML private Button                     btnNuevo;
    @FXML private Button                     btnVolver;

    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
        colHorasAcumuladas.setCellValueFactory(new PropertyValueFactory<>("horasVueloAcumuladas"));

        agregarColumnaAcciones();
    }

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
        recargarTabla();
    }

    // -------------------------------------------------------
    // Tabla
    // -------------------------------------------------------
    private void recargarTabla() {
        Piloto[] arr = aerolinea.listPilotosActivos();
        tblPilotos.setItems(FXCollections.observableArrayList(arr));
        lblEstado.setText("Total pilotos: " + arr.length);
    }

    private void agregarColumnaAcciones() {
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");

            {
                btnEditar.setStyle(
                    "-fx-background-color: #2980B9; -fx-text-fill: white;" +
                    "-fx-font-size: 13px; -fx-font-weight: bold;" +
                    "-fx-background-radius: 8; -fx-cursor: hand;" +
                    "-fx-padding: 4 14 4 14; -fx-border-color: transparent;"
                );
                btnEditar.setOnAction(e -> {
                    Piloto p = getTableView().getItems().get(getIndex());
                    abrirDialogo(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(btnEditar);
                    box.setPadding(new Insets(2, 0, 2, 0));
                    setGraphic(box);
                }
            }
        });
    }

    // -------------------------------------------------------
    // CRUD
    // -------------------------------------------------------
    @FXML
    public void handleNuevoPiloto(ActionEvent event) {
        abrirDialogo(null);
    }

    private void abrirDialogo(Piloto piloto) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(piloto == null ? "Nuevo piloto" : "Editar piloto");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label     lblNombre    = new Label("Nombre:");
        TextField txtNombre    = new TextField(piloto != null ? piloto.getNombre() : "");

        Label     lblTipoDoc   = new Label("Tipo documento:");
        TextField txtTipoDoc   = new TextField(piloto != null ? piloto.getTipoDocumento() : "");

        Label     lblDoc       = new Label("Documento:");
        TextField txtDoc       = new TextField(piloto != null ? piloto.getDocumento() : "");
        if (piloto != null) txtDoc.setDisable(true);

        Label     lblTelefono  = new Label("Teléfono:");
        TextField txtTelefono  = new TextField(piloto != null ? piloto.getTelefono() : "");

        Label     lblEmail     = new Label("Email:");
        TextField txtEmail     = new TextField(piloto != null ? piloto.getEmail() : "");

        Label     lblSalario   = new Label("Salario base:");
        TextField txtSalario   = new TextField(piloto != null ? String.valueOf(piloto.getSalarioBase()) : "");

        Label     lblAnios     = new Label("Años experiencia:");
        TextField txtAnios     = new TextField(piloto != null ? String.valueOf(piloto.getAniosExperiencia()) : "");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(20));
        grid.add(lblNombre,   0, 0); grid.add(txtNombre,   1, 0);
        grid.add(lblTipoDoc,  0, 1); grid.add(txtTipoDoc,  1, 1);
        grid.add(lblDoc,      0, 2); grid.add(txtDoc,      1, 2);
        grid.add(lblTelefono, 0, 3); grid.add(txtTelefono, 1, 3);
        grid.add(lblEmail,    0, 4); grid.add(txtEmail,    1, 4);
        grid.add(lblSalario,  0, 5); grid.add(txtSalario,  1, 5);
        grid.add(lblAnios,    0, 6); grid.add(txtAnios,    1, 6);

        // Contraseña solo en creación
        if (piloto == null) {
            Label     lblPassword = new Label("Contraseña:");
            TextField txtPassword = new TextField();
            grid.add(lblPassword, 0, 7);
            grid.add(txtPassword, 1, 7);

            dialog.getDialogPane().setContent(grid);
            ButtonType btGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
            ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btCancelar);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isEmpty()) return;

            if (result.get() == btGuardar) {
                try {
                    aerolinea.addPiloto(
                        txtNombre.getText().trim(),
                        txtTipoDoc.getText().trim(),
                        txtDoc.getText().trim(),
                        txtTelefono.getText().trim(),
                        txtEmail.getText().trim(),
                        txtPassword.getText().trim(),
                        Double.parseDouble(txtSalario.getText().trim()),
                        new Date(),
                        Integer.parseInt(txtAnios.getText().trim())
                    );
                    aerolinea.guardarPilotos();
                    recargarTabla();
                    estado("✔ Piloto creado correctamente.");
                } catch (NumberFormatException ex) {
                    mostrarError("Salario y años de experiencia deben ser numéricos.");
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }
            }

        } else {
            dialog.getDialogPane().setContent(grid);
            ButtonType btGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
            ButtonType btEliminar = new ButtonType("Eliminar", ButtonBar.ButtonData.LEFT);
            ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btEliminar, btCancelar);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isEmpty()) return;

            if (result.get() == btGuardar) {
                try {
                    piloto.setNombre(txtNombre.getText().trim());
                    piloto.setTipoDocumento(txtTipoDoc.getText().trim());
                    piloto.setTelefono(txtTelefono.getText().trim());
                    piloto.setEmail(txtEmail.getText().trim());
                    piloto.setSalarioBase(Double.parseDouble(txtSalario.getText().trim()));
                    piloto.setAniosExperiencia(Integer.parseInt(txtAnios.getText().trim()));
                    aerolinea.guardarPilotos();
                    recargarTabla();
                    estado("✔ Piloto actualizado.");
                } catch (NumberFormatException ex) {
                    mostrarError("Salario y años de experiencia deben ser numéricos.");
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }

            } else if (result.get() == btEliminar) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar piloto");
                conf.setHeaderText("¿Eliminar piloto " + piloto.getNombre() + "?");
                conf.setContentText("Esta acción puede afectar vuelos asignados a este piloto.");

                Optional<ButtonType> r2 = conf.showAndWait();
                if (r2.isPresent() && r2.get() == ButtonType.OK) {
                    try {
                        aerolinea.deleteEmpleado(piloto.getId());
                        aerolinea.guardarPilotos();
                        recargarTabla();
                        estado("✔ Piloto eliminado.");
                    } catch (Exception ex) {
                        mostrarError(ex.getMessage());
                    }
                }
            }
        }
    }

    // -------------------------------------------------------
    // Navegación
    // -------------------------------------------------------
    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Stage stage   = (Stage) btnVolver.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/MainPageAdminView.fxml")
            );
            Parent root = loader.load();
            MainPageAdminViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            Scene scene = btnVolver.getScene();
            scene.setRoot(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            mostrarError("Error al volver: " + e.getMessage());
        }
    }

    // -------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------
    private void estado(String msg) {
        lblEstado.setText(msg);
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}