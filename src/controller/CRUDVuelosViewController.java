package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import principal.*;
import excepciones.*;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.ResourceBundle;

public class CRUDVuelosViewController implements Initializable {

    // Clase interna para representar una ruta agrupada en la tabla
    public static class Ruta {
        private final String         origenId;
        private final String         destinoId;
        private final String         origenNombre;
        private final String         destinoNombre;
        private final String         frecuencia;
        private final List<Vuelo>    vuelos;

        public Ruta(String origenId, String destinoId, String origenNombre,
                    String destinoNombre, String frecuencia, List<Vuelo> vuelos) {
            this.origenId     = origenId;
            this.destinoId    = destinoId;
            this.origenNombre = origenNombre;
            this.destinoNombre= destinoNombre;
            this.frecuencia   = frecuencia;
            this.vuelos       = vuelos;
        }

        public String getOrigenNombre()  { return origenNombre; }
        public String getDestinoNombre() { return destinoNombre; }
        public String getFrecuencia()    { return frecuencia; }
        public int    getCantidadVuelos(){ return vuelos.size(); }
        public List<Vuelo> getVuelos()   { return vuelos; }
        public String getOrigenId()      { return origenId; }
        public String getDestinoId()     { return destinoId; }
    }

    @FXML private TableView<Ruta>          tblRutas;
    @FXML private TableColumn<Ruta,String> colOrigen;
    @FXML private TableColumn<Ruta,String> colDestino;
    @FXML private TableColumn<Ruta,String> colAvion;
    @FXML private TableColumn<Ruta,String> colFrecuencia;
    @FXML private TableColumn<Ruta,Number> colVuelos;
    @FXML private TableColumn<Ruta,Void>   colAcciones;
    @FXML private Label                    lblEstado;
    @FXML private Button                   btnNuevo;
    @FXML private Button                   btnVolver;

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colOrigen.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getOrigenNombre()));
        colDestino.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getDestinoNombre()));
        colAvion.setCellValueFactory(cd -> {
            List<Vuelo> vs = cd.getValue().getVuelos();
            String modelo = vs.isEmpty() || vs.get(0).getAvion() == null
                    ? "—" : vs.get(0).getAvion().getModelo();
            return new SimpleStringProperty(modelo);
        });
        colFrecuencia.setCellValueFactory(cd ->
            new SimpleStringProperty(cd.getValue().getFrecuencia()));
        colVuelos.setCellValueFactory(cd ->
            new SimpleIntegerProperty(cd.getValue().getCantidadVuelos()));

        agregarColumnaAcciones();
    }

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
        recargarTabla();
    }

    // -------------------------------------------------------
    // Tabla — agrupa vuelos por origen+destino
    // -------------------------------------------------------
    private void recargarTabla() {
        Vuelo[] todos = aerolinea.listVuelosActivos();
        Map<String, Ruta> mapaRutas = new LinkedHashMap<>();

        for (Vuelo v : todos) {
            String clave = v.getOrigen().getId() + "_" + v.getDestino().getId();
            if (!mapaRutas.containsKey(clave)) {
                mapaRutas.put(clave, new Ruta(
                    v.getOrigen().getId(),
                    v.getDestino().getId(),
                    v.getOrigen().getNombre() + " (" + v.getOrigen().getCiudad() + ")",
                    v.getDestino().getNombre() + " (" + v.getDestino().getCiudad() + ")",
                    "—",
                    new ArrayList<>()
                ));
            }
            mapaRutas.get(clave).getVuelos().add(v);
        }

        tblRutas.setItems(FXCollections.observableArrayList(mapaRutas.values()));
        lblEstado.setText("Total rutas: " + mapaRutas.size() +
                          "  |  Total vuelos activos: " + todos.length);
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
                    Ruta r = getTableView().getItems().get(getIndex());
                    abrirDialogoEditar(r);
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
    // Crear ruta — genera vuelos hasta fin de mes
    // -------------------------------------------------------
    @FXML
    public void handleNuevaRuta(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nueva ruta");
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Aeropuertos disponibles
        Aeropuerto[] aeropuertos = aerolinea.listAeropuertosActivos();
        if (aeropuertos.length < 2) {
            mostrarError("Se necesitan al menos 2 aeropuertos registrados.");
            return;
        }

        // Aviones disponibles
        Avion[] aviones = aerolinea.listAvionesActivos();
        if (aviones.length == 0) {
            mostrarError("Se necesita al menos un avión registrado.");
            return;
        }

        ComboBox<Aeropuerto> cbOrigen  = new ComboBox<>(
            FXCollections.observableArrayList(aeropuertos));
        ComboBox<Aeropuerto> cbDestino = new ComboBox<>(
            FXCollections.observableArrayList(aeropuertos));
        ComboBox<Avion>      cbAvion   = new ComboBox<>(
            FXCollections.observableArrayList(aviones));

        cbOrigen.setConverter(aeropuertoConverter());
        cbDestino.setConverter(aeropuertoConverter());
        cbAvion.setConverter(avionConverter());

        // Pilotos y tripulantes
        Piloto[]          pilotos    = aerolinea.listPilotosActivos();
        TripulanteCabina[] tripulantes = aerolinea.listTripulantesActivos();

        if (pilotos.length < 2) {
            mostrarError("Se necesitan al menos 2 pilotos registrados.");
            return;
        }
        if (tripulantes.length == 0) {
            mostrarError("Se necesita al menos un tripulante registrado.");
            return;
        }

        // Frecuencia
        ComboBox<String> cbFrecuencia = new ComboBox<>(FXCollections.observableArrayList(
            "Diariamente", "Cada día intermitente", "Semanalmente",
            "Quincenalmente", "Mensualmente"
        ));
        cbFrecuencia.getSelectionModel().selectFirst();

        Label     lblFreqDiaria = new Label("Vuelos por día:");
        TextField txtFreqDiaria = new TextField("1");

        // Horas — se genera dinámicamente según frecuencia diaria
        VBox boxHoras = new VBox(6);
        Label lblHorasTitle = new Label("Hora(s) de salida (HH:mm):");
        lblHorasTitle.setStyle("-fx-text-fill: #888888; -fx-font-weight: bold;");
        boxHoras.getChildren().add(lblHorasTitle);

        List<TextField> camposHora = new ArrayList<>();
        actualizarCamposHora(boxHoras, camposHora, 1);

        txtFreqDiaria.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                int n = Integer.parseInt(newVal.trim());
                if (n > 0 && n <= 10)
                    actualizarCamposHora(boxHoras, camposHora, n);
            } catch (NumberFormatException ignored) {}
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        grid.add(new Label("Origen:"),            0, 0); grid.add(cbOrigen,      1, 0);
        grid.add(new Label("Destino:"),           0, 1); grid.add(cbDestino,     1, 1);
        grid.add(new Label("Avión:"),             0, 2); grid.add(cbAvion,       1, 2);
        grid.add(new Label("Frecuencia:"),        0, 3); grid.add(cbFrecuencia,  1, 3);
        grid.add(lblFreqDiaria,                   0, 4); grid.add(txtFreqDiaria, 1, 4);
        grid.add(boxHoras,                        0, 5, 2, 1);

        dialog.getDialogPane().setContent(grid);
        ButtonType btGuardar  = new ButtonType("Generar vuelos", ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancelar = new ButtonType("Cancelar",       ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btCancelar);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != btGuardar) return;

        try {
            Aeropuerto origen  = cbOrigen.getValue();
            Aeropuerto destino = cbDestino.getValue();
            Avion      avion   = cbAvion.getValue();

            if (origen == null || destino == null || avion == null)
                throw new EValorNulo("Debes seleccionar origen, destino y avión.");
            if (origen.getId().equals(destino.getId()))
                throw new EValorNulo("El origen y destino no pueden ser iguales.");

            String frecuencia = cbFrecuencia.getValue();

            // Parsear horas
            List<LocalTime> horas = new ArrayList<>();
            for (TextField tf : camposHora) {
                String txt = tf.getText().trim();
                if (txt.isEmpty()) throw new EValorNulo("Todas las horas deben estar completas.");
                horas.add(LocalTime.parse(txt, HORA_FMT));
            }

            // Pilotos (tomar los 2 primeros disponibles — se puede mejorar)
            Piloto[] dospilotos = new Piloto[]{ pilotos[0], pilotos[1] };

            // Tripulantes (al menos 1)
            TripulanteCabina[] unTrip = new TripulanteCabina[]{ tripulantes[0] };

            // Generar vuelos hasta fin de mes
            generarVuelos(origen, destino, avion, dospilotos, unTrip,
                          horas, frecuencia);

            aerolinea.guardarVuelo();
            recargarTabla();
            estado("✔ Vuelos generados correctamente hasta fin de mes.");

        } catch (DateTimeParseException ex) {
            mostrarError("Formato de hora inválido. Usa HH:mm (ej: 07:00).");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void generarVuelos(Aeropuerto origen, Aeropuerto destino, Avion avion,
                                Piloto[] pilotos, TripulanteCabina[] tripulacion,
                                List<LocalTime> horas, String frecuencia) throws Exception {

        LocalDate hoy     = LocalDate.now();
        LocalDate finAno = hoy.withDayOfYear(hoy.lengthOfYear());
        LocalDate cursor  = hoy;
        int       paso    = getPasoFrecuencia(frecuencia);

        while (!cursor.isAfter(finAno)) {
            for (LocalTime hora : horas) {
                LocalDateTime salida = cursor.atTime(hora);
                if (salida.isAfter(LocalDateTime.now())) {
                    aerolinea.addVuelo(origen, destino, salida, avion,
                                       tripulacion, pilotos, 0);
                }
            }
            cursor = cursor.plusDays(paso);
        }
    }

    private int getPasoFrecuencia(String frecuencia) {
        switch (frecuencia) {
            case "Diariamente":           return 1;
            case "Cada día intermitente": return 2;
            case "Semanalmente":          return 7;
            case "Quincenalmente":        return 15;
            case "Mensualmente":          return 30;
            default:                      return 1;
        }
    }

    private void actualizarCamposHora(VBox box, List<TextField> campos, int n) {
        // Mantener título, reemplazar campos
        while (box.getChildren().size() > 1)
            box.getChildren().remove(box.getChildren().size() - 1);
        campos.clear();

        for (int i = 0; i < n; i++) {
            TextField tf = new TextField();
            tf.setPromptText("Vuelo " + (i + 1) + " — ej: 07:00");
            campos.add(tf);
            box.getChildren().add(tf);
        }
    }

    // -------------------------------------------------------
    // Editar ruta — solo afecta al próximo vuelo de la ruta
    // -------------------------------------------------------
    private void abrirDialogoEditar(Ruta ruta) {
        // Buscar próximo vuelo de la ruta
        Vuelo proximo = ruta.getVuelos().stream()
            .filter(v -> v.getFechaHoraSalida().isAfter(LocalDateTime.now()))
            .min(Comparator.comparing(Vuelo::getFechaHoraSalida))
            .orElse(null);

        if (proximo == null) {
            mostrarError("No hay vuelos próximos en esta ruta para editar.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar próximo vuelo — " +
            ruta.getOrigenNombre() + " → " + ruta.getDestinoNombre());
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Avión
        Avion[] aviones = aerolinea.listAvionesActivos();
        ComboBox<Avion> cbAvion = new ComboBox<>(
            FXCollections.observableArrayList(aviones));
        cbAvion.setConverter(avionConverter());
        if (proximo.getAvion() != null) cbAvion.setValue(proximo.getAvion());

        // Puerta de embarque
        Label     lblPuerta = new Label("Puerta de embarque:");
        TextField txtPuerta = new TextField(
            proximo.getPuertaEmbarque() != null ? proximo.getPuertaEmbarque() : "");

        // Atrasar vuelo
        Label     lblNuevaSalida = new Label("Nueva hora salida (dejar vacío = sin cambio):");
        TextField txtNuevaSalida = new TextField();
        txtNuevaSalida.setPromptText("dd/MM/yyyy HH:mm");

        // Estado — cancelar
        Label    lblCancelar = new Label("Cancelar este vuelo:");
        CheckBox chkCancelar = new CheckBox();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Info no editable
        Label lblInfo = new Label(
            "Vuelo: " + proximo.getId() +
            "  |  Salida actual: " + proximo.getFechaHoraSalida()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );
        lblInfo.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px;");

        grid.add(lblInfo,                         0, 0, 2, 1);
        grid.add(new Label("Avión:"),             0, 1); grid.add(cbAvion,        1, 1);
        grid.add(lblPuerta,                       0, 2); grid.add(txtPuerta,      1, 2);
        grid.add(lblNuevaSalida,                  0, 3); grid.add(txtNuevaSalida, 1, 3);
        grid.add(lblCancelar,                     0, 4); grid.add(chkCancelar,    1, 4);

        dialog.getDialogPane().setContent(grid);
        ButtonType btGuardar  = new ButtonType("Guardar",  ButtonBar.ButtonData.OK_DONE);
        ButtonType btCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(btGuardar, btCancelar);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != btGuardar) return;

        try {
            // Avión
            if (cbAvion.getValue() != null)
                proximo.setAvion(cbAvion.getValue());

            // Puerta
            String puerta = txtPuerta.getText().trim();
            if (!puerta.isEmpty())
                proximo.setPuertaEmbarque(puerta);

            // Atrasar
            String nuevaSalidaTxt = txtNuevaSalida.getText().trim();
            if (!nuevaSalidaTxt.isEmpty()) {
                LocalDateTime nuevaSalida = LocalDateTime.parse(
                    nuevaSalidaTxt,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                );
                proximo.setAtrasado(nuevaSalida);
            }

            // Cancelar
            if (chkCancelar.isSelected())
                proximo.cancelado();

            aerolinea.guardarVuelo();
            recargarTabla();
            estado("✔ Próximo vuelo de la ruta actualizado.");

        } catch (DateTimeParseException ex) {
            mostrarError("Formato de fecha inválido. Usa dd/MM/yyyy HH:mm");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    // -------------------------------------------------------
    // Converters para ComboBox
    // -------------------------------------------------------
    private javafx.util.StringConverter<Aeropuerto> aeropuertoConverter() {
        return new javafx.util.StringConverter<>() {
            @Override public String toString(Aeropuerto a) {
                return a == null ? "" : a.getNombre() + " (" + a.getCiudad() + ")";
            }
            @Override public Aeropuerto fromString(String s) { return null; }
        };
    }

    private javafx.util.StringConverter<Avion> avionConverter() {
        return new javafx.util.StringConverter<>() {
            @Override public String toString(Avion a) {
                return a == null ? "" : a.getMatricula() + " — " + a.getModelo();
            }
            @Override public Avion fromString(String s) { return null; }
        };
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