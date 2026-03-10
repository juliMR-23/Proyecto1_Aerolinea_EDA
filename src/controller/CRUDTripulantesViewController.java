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
import javafx.util.Callback;
import principal.Aerolinea;
import principal.TripulanteCabina;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class CRUDTripulantesViewController implements Initializable {

    @FXML private TableView<TripulanteCabina>          tblTripulantes;
    @FXML private TableColumn<TripulanteCabina,String>  colId;
    @FXML private TableColumn<TripulanteCabina,String>  colNombre;
    @FXML private TableColumn<TripulanteCabina,String>  colDocumento;
    @FXML private TableColumn<TripulanteCabina,String>  colEmail;
    @FXML private TableColumn<TripulanteCabina,Double>  colSalario;
    @FXML private TableColumn<TripulanteCabina,Double>  colHorasAcumuladas;
    @FXML private TableColumn<TripulanteCabina,Integer> colIdiomas;
    @FXML private TableColumn<TripulanteCabina,Void>    colAcciones;
    @FXML private Label                                 lblEstado;
    @FXML private Button                                btnNuevo;
    @FXML private Button                                btnVolver;

    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
        colHorasAcumuladas.setCellValueFactory(new PropertyValueFactory<>("horasVueloAcumuladas"));

        // Columna idiomas muestra cantidad
        colIdiomas.setCellValueFactory(cd ->
            new javafx.beans.property.SimpleIntegerProperty(
                cd.getValue().listIdiomas().length
            ).asObject()
        );

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
        TripulanteCabina[] arr = aerolinea.listTripulantesActivos();
        tblTripulantes.setItems(FXCollections.observableArrayList(arr));
        lblEstado.setText("Total tripulantes: " + arr.length);
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
                    TripulanteCabina t = getTableView().getItems().get(getIndex());
                    abrirDialogo(t);
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
    public void handleNuevoTripulante(ActionEvent event) {
        abrirDialogo(null);
    }

    private void abrirDialogo(TripulanteCabina tripulante) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(tripulante == null ? "Nuevo tripulante" : "Editar tripulante");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label     lblNombre   = new Label("Nombre:");
        TextField txtNombre   = new TextField(tripulante != null ? tripulante.getNombre() : "");

        Label     lblTipoDoc  = new Label("Tipo documento:");
        TextField txtTipoDoc  = new TextField(tripulante != null ? tripulante.getTipoDocumento() : "");

        Label     lblDoc      = new Label("Documento:");
        TextField txtDoc      = new TextField(tripulante != null ? tripulante.getDocumento() : "");
        if (tripulante != null) txtDoc.setDisable(true);

        Label     lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField(tripulante != null ? tripulante.getTelefono() : "");

        Label     lblEmail    = new Label("Email:");
        TextField txtEmail    = new TextField(tripulante != null ? tripulante.getEmail() : "");

        Label     lblSalario  = new Label("Salario base:");
        TextField txtSalario  = new TextField(tripulante != null ? String.valueOf(tripulante.getSalarioBase()) : "");

        Label     lblAnios    = new Label("Años experiencia:");
        TextField txtAnios    = new TextField(tripulante != null ? String.valueOf(tripulante.getAniosExperiencia()) : "");

        // Idiomas — separados por coma
        Label     lblIdiomas  = new Label("Idiomas (separados por coma):");
        String    idiomasStr  = tripulante != null
                ? String.join(", ", tripulante.listIdiomas())
                : "";
        TextField txtIdiomas  = new TextField(idiomasStr);

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
        grid.add(lblIdiomas,  0, 7); grid.add(txtIdiomas,  1, 7);

        if (tripulante == null) {
            Label     lblPassword = new Label("Contraseña:");
            TextField txtPassword = new TextField();
            grid.add(lblPassword, 0, 8);
            grid.add(txtPassword, 1, 8);

            dialog.getDialogPane().setContent(grid);
            ButtonType btGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
            ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btCancelar);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isEmpty()) return;

            if (result.get() == btGuardar) {
                try {
                	aerolinea.addTripulanteCabina(
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
                		TripulanteCabina[] todos = aerolinea.listTripulantesActivos();
                		TripulanteCabina nuevo = todos[todos.length - 1];
                		agregarIdiomas(nuevo, txtIdiomas.getText());
                    aerolinea.guardarTripulantesCabina();
                    recargarTabla();
                    estado("✔ Tripulante creado correctamente.");
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
                    tripulante.setNombre(txtNombre.getText().trim());
                    tripulante.setTipoDocumento(txtTipoDoc.getText().trim());
                    tripulante.setTelefono(txtTelefono.getText().trim());
                    tripulante.setEmail(txtEmail.getText().trim());
                    tripulante.setSalarioBase(Double.parseDouble(txtSalario.getText().trim()));
                    tripulante.setAniosExperiencia(Integer.parseInt(txtAnios.getText().trim()));

                    // Reemplazar idiomas completamente
                    tripulante.setIdiomas(new String[0]);
                    agregarIdiomas(tripulante, txtIdiomas.getText());

                    aerolinea.guardarTripulantesCabina();
                    recargarTabla();
                    estado("✔ Tripulante actualizado.");
                } catch (NumberFormatException ex) {
                    mostrarError("Salario y años de experiencia deben ser numéricos.");
                } catch (Exception ex) {
                    mostrarError(ex.getMessage());
                }

            } else if (result.get() == btEliminar) {
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar tripulante");
                conf.setHeaderText("¿Eliminar tripulante " + tripulante.getNombre() + "?");
                conf.setContentText("Esta acción puede afectar vuelos asignados a este tripulante.");

                Optional<ButtonType> r2 = conf.showAndWait();
                if (r2.isPresent() && r2.get() == ButtonType.OK) {
                    try {
                        aerolinea.deleteEmpleado(tripulante.getId());
                        aerolinea.guardarTripulantesCabina();
                        recargarTabla();
                        estado("✔ Tripulante eliminado.");
                    } catch (Exception ex) {
                        mostrarError(ex.getMessage());
                    }
                }
            }
        }
    }

    private void agregarIdiomas(TripulanteCabina t, String texto) throws Exception {
        if (texto == null || texto.isBlank()) return;
        String[] partes = texto.split(",");
        for (String idioma : partes) {
            String trimmed = idioma.trim();
            if (!trimmed.isEmpty())
                t.addIdioma(trimmed);
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