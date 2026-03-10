package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import principal.*;

import java.net.URL;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class ComprarVueloViewController implements Initializable {

    @FXML private Label  lblResumenVuelo;
    @FXML private Label  lblHoraSalida;
    @FXML private Label  lblHoraLlegada;
    @FXML private Label  lblCodOrigen;
    @FXML private Label  lblCodDestino;
    @FXML private Label  lblDuracion;
    @FXML private Label  lblPrecioPorTiquete;
    @FXML private Label  lblTotal;
    @FXML private Label  lblMensaje;
    @FXML private VBox   vboxPasajeros;
    @FXML private Button btnConfirmar;
    @FXML private Button btnVolver;

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private Vuelo     vuelo;
    private Cliente   clienteLogueado;
    private Aerolinea aerolinea;
    private int       numeroPasajeros;

    private TextField[] camposNombre;
    private TextField[] camposTipoDoc;
    private TextField[] camposNumDoc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public void setDatos(Vuelo vuelo, int numeroPasajeros, Cliente clienteLogueado) {
        this.vuelo           = vuelo;
        this.numeroPasajeros = numeroPasajeros;
        this.clienteLogueado = clienteLogueado;

        camposNombre  = new TextField[numeroPasajeros];
        camposTipoDoc = new TextField[numeroPasajeros];
        camposNumDoc  = new TextField[numeroPasajeros];

        poblarResumenVuelo();
        generarFormulariosPasajeros();
        actualizarTotal();
    }

    private void poblarResumenVuelo() {
        LocalDateTime salida  = vuelo.getFechaHoraSalida();
        LocalDateTime llegada = vuelo.getFechaHoraLlegada() != null
                ? vuelo.getFechaHoraLlegada()
                : salida.plusMinutes(75);

        Duration dur = Duration.between(salida, llegada);

        lblHoraSalida.setText(salida.format(HORA_FMT));
        lblHoraLlegada.setText(llegada.format(HORA_FMT));
        lblCodOrigen.setText(abrev(vuelo.getOrigen().getNombre()));
        lblCodDestino.setText(abrev(vuelo.getDestino().getNombre()));
        lblDuracion.setText(dur.toHours() + "h " + dur.toMinutesPart() + "m");
        lblResumenVuelo.setText(
            vuelo.getOrigen().getCiudad() + "  →  " +
            vuelo.getDestino().getCiudad() + "  |  " +
            salida.toLocalDate()
        );

        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "CO"));
        lblPrecioPorTiquete.setText("COP " + nf.format((long) vuelo.getPrecio()));
    }

    private void generarFormulariosPasajeros() {
        vboxPasajeros.getChildren().clear();

        for (int i = 0; i < numeroPasajeros; i++) {
            VBox card = new VBox(12);
            card.setPadding(new Insets(24, 32, 24, 32));
            card.setStyle(
                "-fx-background-color: #2C3E50;" +
                "-fx-background-radius: 14;"
            );

            Label titulo = new Label(i == 0 ? "Pasajero 1 (tú)" : "Pasajero " + (i + 1));
            titulo.setStyle("-fx-text-fill: #C9A84C; -fx-font-size: 16px; -fx-font-weight: bold;");

            VBox boxNombre = crearCampo("Nombre completo",            360);
            VBox boxTipo   = crearCampo("Tipo doc. (CC / CE / PAS)", 200);
            VBox boxNum    = crearCampo("Número de documento",        240);

            TextField tfNombre = (TextField) boxNombre.getChildren().get(1);
            TextField tfTipo   = (TextField) boxTipo.getChildren().get(1);
            TextField tfNum    = (TextField) boxNum.getChildren().get(1);

            if (i == 0 && clienteLogueado != null) {
                tfNombre.setText(clienteLogueado.getNombre());
                tfTipo.setText(clienteLogueado.getTipoDocumento());
                tfNum.setText(clienteLogueado.getDocumento());
            }

            camposNombre[i]  = tfNombre;
            camposTipoDoc[i] = tfTipo;
            camposNumDoc[i]  = tfNum;

            HBox fila = new HBox(20);
            fila.getChildren().addAll(boxNombre, boxTipo, boxNum);
            card.getChildren().addAll(titulo, fila);
            vboxPasajeros.getChildren().add(card);
        }
    }

    private VBox crearCampo(String etiqueta, double ancho) {
        VBox box = new VBox(6);
        Label lbl = new Label(etiqueta);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888; -fx-font-weight: bold;");
        TextField tf = new TextField();
        tf.setPrefWidth(ancho);
        tf.setPrefHeight(44);
        tf.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-background-color: #1E2A35;" +
            "-fx-text-fill: #ECF0F1;" +
            "-fx-border-color: #C9A84C33;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 0 12 0 12;"
        );
        box.getChildren().addAll(lbl, tf);
        return box;
    }

    private void actualizarTotal() {
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "CO"));
        lblTotal.setText("COP " + nf.format((long)(vuelo.getPrecio() * numeroPasajeros)));
    }

    @FXML
    public void handleConfirmar(ActionEvent event) {
        for (int i = 0; i < numeroPasajeros; i++) {
            if (camposNombre[i].getText().trim().isEmpty() ||
                camposTipoDoc[i].getText().trim().isEmpty() ||
                camposNumDoc[i].getText().trim().isEmpty()) {
                mostrarMensaje("⚠ Completa los datos del pasajero " + (i + 1) + ".", "#C0392B");
                return;
            }
        }
        if (clienteLogueado == null) {
            mostrarMensaje("⚠ Debes iniciar sesión para confirmar la compra.", "#C0392B");
            return;
        }

        try {
            clienteLogueado.addReserva(vuelo);
            Reserva reserva = clienteLogueado.getReservas()[clienteLogueado.getReservas().length - 1];

            String[] ocupados      = getAsientosOcupados();
            String[] todosAsientos = generarAsientos(vuelo.getAvion().getCapacidad());

            for (int i = 0; i < numeroPasajeros; i++) {
                String nombre  = camposNombre[i].getText().trim();
                String tipoDoc = camposTipoDoc[i].getText().trim();
                String numDoc  = camposNumDoc[i].getText().trim();
                String asiento = siguienteAsientoLibre(todosAsientos, ocupados);

                ocupados = Arrays.copyOf(ocupados, ocupados.length + 1);
                ocupados[ocupados.length - 1] = asiento;

                clienteLogueado.addTiqueteOnReserva(reserva, asiento, nombre, numDoc, tipoDoc);
            }

            // Guardar cliente actualizado en su fichero
            aerolinea.guardarClientes();
            clienteLogueado.guardarReservas();
            for(Reserva r: clienteLogueado.getReservas()){
                r.guardarTiquetes();
            }

            NumberFormat nf = NumberFormat.getInstance(new Locale("es", "CO"));
            mostrarMensaje(
                "✔ Compra confirmada. Reserva: " + reserva.getId() +
                "  |  " + numeroPasajeros + " tiquete(s)  |  Total: COP " +
                nf.format((long)(vuelo.getPrecio() * numeroPasajeros)),
                "#27AE60"
            );
            btnConfirmar.setDisable(true);

        } catch (Exception e) {
            mostrarMensaje("⚠ Error al confirmar: " + e.getMessage(), "#C0392B");
        }
    }

    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Stage stage   = (Stage) btnVolver.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/BuscarVuelosView.fxml")
            );
            Parent root = loader.load();
            BuscarVuelosViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            if (clienteLogueado != null) ctrl.setUsuarioLogueado(clienteLogueado);
            Scene scene = btnVolver.getScene();
            scene.setRoot(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            mostrarMensaje("⚠ Error al volver: " + e.getMessage(), "#C0392B");
        }
    }

    private String[] getAsientosOcupados() {
        Reserva[] reservas = vuelo.getReservas();
        int total = 0;
        for (Reserva r : reservas)
            total += r.getTiquetes().length;

        String[] ocupados = new String[total];
        int idx = 0;
        for (Reserva r : reservas)
            for (Tiquete t : r.getTiquetes())
                ocupados[idx++] = t.getAsiento();
        return ocupados;
    }

    private String[] generarAsientos(int capacidad) {
        char[] cols  = {'A','B','C','D','E','F'};
        int    filas = (int) Math.ceil(capacidad / 6.0);
        String[] lista = new String[filas * cols.length];
        int idx = 0;
        for (int f = 1; f <= filas; f++)
            for (char c : cols)
                lista[idx++] = f + String.valueOf(c);
        return lista;
    }

    private String siguienteAsientoLibre(String[] todos, String[] ocupados) {
        for (String asiento : todos) {
            boolean libre = true;
            for (String o : ocupados) {
                if (asiento.equals(o)) { libre = false; break; }
            }
            if (libre) return asiento;
        }
        throw new IllegalStateException("No hay asientos disponibles.");
    }

    private String abrev(String nombre) {
        if (nombre == null) return "N/A";
        return nombre.length() >= 3
                ? nombre.substring(0, 3).toUpperCase()
                : nombre.toUpperCase();
    }

    private void mostrarMensaje(String msg, String color) {
        lblMensaje.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 18px;");
        lblMensaje.setText(msg);
    }
}