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
import principal.Cliente;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CRUDClientesViewController implements Initializable {

    @FXML private TableView<Cliente>          tblClientes;
    @FXML private TableColumn<Cliente,String> colId;
    @FXML private TableColumn<Cliente,String> colNombre;
    @FXML private TableColumn<Cliente,String> colDocumento;
    @FXML private TableColumn<Cliente,String> colTelefono;
    @FXML private TableColumn<Cliente,String> colEmail;
    @FXML private TableColumn<Cliente,Void>   colAcciones;
    @FXML private Label                       lblEstado;
    @FXML private Button                      btnNuevo;
    @FXML private Button                      btnVolver;

    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

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
        Cliente[] arr = aerolinea.listClientesActivos();
        tblClientes.setItems(FXCollections.observableArrayList(arr));
        lblEstado.setText("Total clientes: " + arr.length);
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
                    Cliente c = getTableView().getItems().get(getIndex());
                    abrirDialogo(c);
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
    public void handleNuevoCliente(ActionEvent event) {
        abrirDialogo(null);
    }

    private void abrirDialogo(Cliente cliente) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(cliente == null ? "Nuevo cliente" : "Editar cliente");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label     lblNombre   = new Label("Nombre:");
        TextField txtNombre   = new TextField(cliente != null ? cliente.getNombre() : "");

        Label     lblTipoDoc  = new Label("Tipo documento:");
        TextField txtTipoDoc  = new TextField(cliente != null ? cliente.getTipoDocumento() : "");

        Label     lblDoc      = new Label("Documento:");
        TextField txtDoc      = new TextField(cliente != null ? cliente.getDocumento() : "");
        // Documento no editable en edición, es identificador del cliente
        if (cliente != null) txtDoc.setDisable(true);

        Label     lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField(cliente != null ? cliente.getTelefono() : "");

        Label     lblEmail    = new Label("Email:");
        TextField txtEmail    = new TextField(cliente != null ? cliente.getEmail() : "");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(20));
        grid.add(lblNombre,   0, 0); grid.add(txtNombre,   1, 0);
        grid.add(lblTipoDoc,  0, 1); grid.add(txtTipoDoc,  1, 1);
        grid.add(lblDoc,      0, 2); grid.add(txtDoc,      1, 2);
        grid.add(lblTelefono, 0, 3); grid.add(txtTelefono, 1, 3);
        grid.add(lblEmail,    0, 4); grid.add(txtEmail,    1, 4);

        // Contraseña solo en creación, el admin no puede editarla
        if (cliente == null) {
            Label     lblPassword = new Label("Contraseña:");
            TextField txtPassword = new TextField();
            grid.add(lblPassword, 0, 5);
            grid.add(txtPassword, 1, 5);

            dialog.getDialogPane().setContent(grid);
            ButtonType btGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
            ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btCancelar);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isEmpty()) return;

            if (result.get() == btGuardar) {
                try {
                    aerolinea.addCliente(
                        txtNombre.getText().trim(),
                        txtTipoDoc.getText().trim(),
                        txtDoc.getText().trim(),
                        txtTelefono.getText().trim(),
                        txtEmail.getText().trim(),
                        txtPassword.getText().trim()
                    );
                    aerolinea.guardarClientes();
                    recargarTabla();
                    estado("✔ Cliente creado correctamente.");
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
                    cliente.setNombre(txtNombre.getText().trim());
                    cliente.setTipoDocumento(txtTipoDoc.getText().trim());
                    cliente.setTelefono(txtTelefono.getText().trim());
                    cliente.setEmail(txtEmail.getText().trim());
                    aerolinea.guardarClientes();
                    recargarTabla();
                    estado("✔ Cliente actualizado.");
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }

            } else if (result.get() == btEliminar) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar cliente");
                conf.setHeaderText("¿Eliminar cliente " + cliente.getNombre() + "?");
                conf.setContentText("Esta acción eliminará también sus reservas asociadas.");

                Optional<ButtonType> r2 = conf.showAndWait();
                if (r2.isPresent() && r2.get() == ButtonType.OK) {
                    try {
                        aerolinea.deleteCliente(cliente.getId());
                        aerolinea.guardarClientes();
                        recargarTabla();
                        estado("✔ Cliente eliminado.");
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