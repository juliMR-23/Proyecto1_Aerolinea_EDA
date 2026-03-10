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
import principal.Avion;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CRUDAvionesViewController implements Initializable {

    @FXML private TableView<Avion>          tblAviones;
    @FXML private TableColumn<Avion,String>  colMatricula;
    @FXML private TableColumn<Avion,String>  colMarca;
    @FXML private TableColumn<Avion,String>  colModelo;
    @FXML private TableColumn<Avion,Integer> colCapacidad;
    @FXML private TableColumn<Avion,Double>  colVelocidad;
    @FXML private TableColumn<Avion,Boolean> colDisponible;
    @FXML private TableColumn<Avion,Void>    colAcciones;
    @FXML private Label                      lblEstado;
    @FXML private Button                     btnNuevo;
    @FXML private Button                     btnVolver;

    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colCapacidad.setCellValueFactory(new PropertyValueFactory<>("capacidad"));
        colVelocidad.setCellValueFactory(new PropertyValueFactory<>("velocidad"));
        colDisponible.setCellValueFactory(new PropertyValueFactory<>("disponible"));

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
        Avion[] arr = aerolinea.listAvionesActivos();
        tblAviones.setItems(FXCollections.observableArrayList(arr));
        lblEstado.setText("Total aviones: " + arr.length);
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
                    Avion a = getTableView().getItems().get(getIndex());
                    abrirDialogo(a);
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
    public void handleNuevoAvion(ActionEvent event) {
        abrirDialogo(null);
    }

    private void abrirDialogo(Avion avion) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(avion == null ? "Nuevo avión" : "Editar avión");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label     lblMatricula  = new Label("Matrícula:");
        TextField txtMatricula  = new TextField(avion != null ? avion.getMatricula() : "");

        Label     lblMarca      = new Label("Marca:");
        TextField txtMarca      = new TextField(avion != null ? avion.getMarca() : "");

        Label     lblModelo     = new Label("Modelo:");
        TextField txtModelo     = new TextField(avion != null ? avion.getModelo() : "");

        Label     lblCapacidad  = new Label("Capacidad:");
        TextField txtCapacidad  = new TextField(avion != null ? String.valueOf(avion.getCapacidad()) : "");

        Label     lblVelocidad  = new Label("Velocidad (kt):");
        TextField txtVelocidad  = new TextField(avion != null ? String.valueOf(avion.getVelocidad()) : "");

        Label          lblDisponible = new Label("Disponible:");
        CheckBox       chkDisponible = new CheckBox();
        chkDisponible.setSelected(avion == null || avion.isDisponible());

        // Matrícula no editable si es edición
        if (avion != null) txtMatricula.setDisable(true);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(20));
        grid.add(lblMatricula,  0, 0); grid.add(txtMatricula,  1, 0);
        grid.add(lblMarca,      0, 1); grid.add(txtMarca,      1, 1);
        grid.add(lblModelo,     0, 2); grid.add(txtModelo,     1, 2);
        grid.add(lblCapacidad,  0, 3); grid.add(txtCapacidad,  1, 3);
        grid.add(lblVelocidad,  0, 4); grid.add(txtVelocidad,  1, 4);
        grid.add(lblDisponible, 0, 5); grid.add(chkDisponible, 1, 5);

        dialog.getDialogPane().setContent(grid);

        ButtonType btGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
        ButtonType btEliminar = new ButtonType("Eliminar", ButtonBar.ButtonData.LEFT);
        ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (avion == null) {
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btCancelar);
        } else {
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btEliminar, btCancelar);
        }

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        if (result.get() == btGuardar) {
            try {
                String matricula  = txtMatricula.getText().trim();
                String marca      = txtMarca.getText().trim();
                String modelo     = txtModelo.getText().trim();
                int    capacidad  = Integer.parseInt(txtCapacidad.getText().trim());
                double velocidad  = Double.parseDouble(txtVelocidad.getText().trim());
                boolean disponible = chkDisponible.isSelected();

                if (avion == null) {
                    aerolinea.addAvion(matricula, marca, modelo, capacidad, velocidad);
                    estado("✔ Avión creado correctamente.");
                } else {
                    avion.setMarca(marca);
                    avion.setModelo(modelo);
                    avion.setCapacidad(capacidad);
                    avion.setVelocidad(velocidad);
                    avion.setDisponible(disponible);
                    estado("✔ Avión actualizado.");
                }

                aerolinea.guardarAviones();
                recargarTabla();

            } catch (NumberFormatException ex) {
                mostrarError("Capacidad debe ser entero y velocidad debe ser numérico.");
            } catch (Exception ex) {
                mostrarError(ex.getMessage());
            }

        } else if (result.get() == btEliminar && avion != null) {
            Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
            conf.setTitle("Eliminar avión");
            conf.setHeaderText("¿Eliminar avión " + avion.getMatricula() + "?");
            conf.setContentText("Esta acción puede afectar vuelos asociados.");

            Optional<ButtonType> r2 = conf.showAndWait();
            if (r2.isPresent() && r2.get() == ButtonType.OK) {
                try {
                    aerolinea.deleteAvion(avion.getMatricula());
                    aerolinea.guardarAviones();
                    recargarTabla();
                    estado("✔ Avión eliminado.");
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
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