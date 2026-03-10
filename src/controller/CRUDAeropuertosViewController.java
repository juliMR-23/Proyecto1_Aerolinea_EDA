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
import principal.Aeropuerto;
import principal.SesionAerolinea;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CRUDAeropuertosViewController implements Initializable {

    @FXML private TableView<Aeropuerto>          tblAeropuertos;
    @FXML private TableColumn<Aeropuerto,String> colId;
    @FXML private TableColumn<Aeropuerto,String> colNombre;
    @FXML private TableColumn<Aeropuerto,String> colCiudad;
    @FXML private TableColumn<Aeropuerto,String> colPais;
    @FXML private TableColumn<Aeropuerto,String> colZona;
    @FXML private TableColumn<Aeropuerto,Void>   colAcciones;
    @FXML private Label                          lblEstado;
    @FXML private Button                         btnNuevo;
    @FXML private Button                         btnVolver;

    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        aerolinea = SesionAerolinea.getAerolinea();

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCiudad.setCellValueFactory(new PropertyValueFactory<>("ciudad"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colZona.setCellValueFactory(new PropertyValueFactory<>("zonaHoraria"));

        agregarColumnaAcciones();
        recargarTabla();
    }

    private void recargarTabla() {
        if (aerolinea == null) {
            tblAeropuertos.setItems(FXCollections.observableArrayList());
            lblEstado.setText("Total aeropuertos: 0");
            return;
        }
        Aeropuerto[] arr = aerolinea.listAeropuertos();
        tblAeropuertos.setItems(FXCollections.observableArrayList(arr));
        lblEstado.setText("Total aeropuertos: " + arr.length);
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
                    Aeropuerto a = getTableView().getItems().get(getIndex());
                    abrirDialogoAeropuerto(a);
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

    @FXML
    public void handleNuevoAeropuerto(ActionEvent event) {
        abrirDialogoAeropuerto(null);
    }

    private void abrirDialogoAeropuerto(Aeropuerto aeropuerto) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(aeropuerto == null ? "Nuevo aeropuerto" : "Editar aeropuerto");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField(aeropuerto != null ? aeropuerto.getNombre() : "");

        Label lblCiudad = new Label("Ciudad:");
        TextField txtCiudad = new TextField(aeropuerto != null ? aeropuerto.getCiudad() : "");

        Label lblPais = new Label("País:");
        TextField txtPais = new TextField(aeropuerto != null ? aeropuerto.getPais() : "");

        Label lblZona = new Label("Zona horaria:");
        TextField txtZona = new TextField(aeropuerto != null ? aeropuerto.getZonaHoraria() : "");

        Label lblLon = new Label("Longitud:");
        TextField txtLon = new TextField(aeropuerto != null ? String.valueOf(aeropuerto.getLongitud()) : "");

        Label lblLat = new Label("Latitud:");
        TextField txtLat = new TextField(aeropuerto != null ? String.valueOf(aeropuerto.getLatitud()) : "");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(20));

        grid.add(lblNombre, 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(lblCiudad, 0, 1); grid.add(txtCiudad, 1, 1);
        grid.add(lblPais,   0, 2); grid.add(txtPais,   1, 2);
        grid.add(lblZona,   0, 3); grid.add(txtZona,   1, 3);
        grid.add(lblLon,    0, 4); grid.add(txtLon,    1, 4);
        grid.add(lblLat,    0, 5); grid.add(txtLat,    1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType btGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        ButtonType btEliminar = new ButtonType("Eliminar", ButtonBar.ButtonData.LEFT);
        ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (aeropuerto == null) {
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btCancelar);
        } else {
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btEliminar, btCancelar);
        }

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        if (result.get() == btGuardar) {
            try {
                String nombre = txtNombre.getText().trim();
                String ciudad = txtCiudad.getText().trim();
                String pais   = txtPais.getText().trim();
                String zona   = txtZona.getText().trim();
                double lon    = Double.parseDouble(txtLon.getText().trim());
                double lat    = Double.parseDouble(txtLat.getText().trim());

                if (aerolinea == null) return;

                if (aeropuerto == null) {
                    aerolinea.addAeropuerto(nombre, ciudad, pais, zona, lon, lat);
                    guardarAerolinea();
                    lblEstado.setText("Aeropuerto creado correctamente.");
                } else {
                    aeropuerto.setNombre(nombre);
                    aeropuerto.setZonaHoraria(zona);
                    guardarAerolinea();
                    lblEstado.setText("Aeropuerto actualizado.");
                }
                recargarTabla();
            } catch (NumberFormatException ex) {
                mostrarError("Longitud y latitud deben ser numéricos.");
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
                ex.printStackTrace();
            }
        } else if (result.get() == btEliminar && aeropuerto != null) {
            Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
            conf.setTitle("Eliminar aeropuerto");
            conf.setHeaderText("¿Eliminar aeropuerto " + aeropuerto.getNombre() + "?");
            conf.setContentText("Esta acción puede afectar vuelos asociados.");

            Optional<ButtonType> r2 = conf.showAndWait();
            if (r2.isPresent() && r2.get() == ButtonType.OK) {
                try {
                    aerolinea.deleteAeropuerto(aeropuerto.getId());
                    guardarAerolinea();
                    recargarTabla();
                    lblEstado.setText("Aeropuerto eliminado.");
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    private void guardarAerolinea() {
        try {
            aerolinea.wFicheroAerolinea("aerolinea.dat");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("No se pudo guardar la aerolínea: " + e.getMessage());
        }
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/MainPageAdminView.fxml")
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
            e.printStackTrace();
        }
    }
}
