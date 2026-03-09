// Creado con SceneBuilder
//TODO: Posibles optimizaciones
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import principal.*;
import excepciones.*;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class VerReservasViewController implements Initializable {

    @FXML private Label  lblSubtitulo;
    @FXML private VBox   vboxProximos;
    @FXML private VBox   vboxPasados;
    @FXML private Button btnVolver;

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");

    private Cliente clienteLogueado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void setCliente(Cliente cliente) {
        this.clienteLogueado = cliente;
        lblSubtitulo.setText(
            "Bienvenido, " + cliente.getNombre() +
            "  —  " + cliente.getReservas().length + " reserva(s) en total"
        );
        cargarReservas();
    }

    private void cargarReservas() {
        vboxProximos.getChildren().clear();
        vboxPasados.getChildren().clear();

        Reserva[]     reservas = clienteLogueado.getReservas();
        LocalDateTime ahora    = LocalDateTime.now();

        int proxCount   = 0;
        int pasadoCount = 0;

        for (Reserva r : reservas) {
            Vuelo vuelo = r.getVuelo();
            if (vuelo == null) continue;

            if (vuelo.getFechaHoraSalida().isAfter(ahora)) {
                vboxProximos.getChildren().add(crearTarjetaReserva(r, false));
                proxCount++;
            } else {
                vboxPasados.getChildren().add(crearTarjetaReserva(r, true));
                pasadoCount++;
            }
        }

        if (proxCount == 0)
            vboxProximos.getChildren().add(mensajeVacio("No tienes vuelos próximos."));
        if (pasadoCount == 0)
            vboxPasados.getChildren().add(mensajeVacio("No tienes vuelos pasados."));
    }

    private VBox crearTarjetaReserva(Reserva reserva, boolean pasado) {
        Vuelo        vuelo    = reserva.getVuelo();
        Tiquete[]    tiquetes = reserva.getTiquetes();
        String       estado   = vuelo.getEstadoVuelo();
        double       total    = reserva.calcularPrecioTotal();
        NumberFormat nf       = NumberFormat.getInstance(new Locale("es", "CO"));

        // Card
        VBox card = new VBox(0);
        card.setStyle(
            "-fx-background-color: " + (pasado ? "#243342" : "#FFFFFF") + ";" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 3, 3);"
        );

        //Header
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20, 32, 20, 32));
        header.setStyle(
            "-fx-background-color: " + (pasado ? "#2C3E50" : "#1E2A35") + ";" +
            "-fx-background-radius: 16 16 0 0;"
        );

        VBox infoRuta = new VBox(4);
        Label lblRuta = new Label(
            vuelo.getOrigen().getNombre() + "  →  " + vuelo.getDestino().getNombre()
        );
        lblRuta.setStyle(
            "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #ECF0F1;"
        );

        Label lblCods = new Label(
            "(" + abrev(vuelo.getOrigen().getNombre()) +
            " → " + abrev(vuelo.getDestino().getNombre()) + ")"
        );
        lblCods.setStyle("-fx-font-size: 14px; -fx-text-fill: #888888;");

        Label lblId = new Label("Reserva: " + reserva.getId());
        lblId.setStyle("-fx-font-size: 13px; -fx-text-fill: #7F8C8D;");

        infoRuta.getChildren().addAll(lblRuta, lblCods, lblId);

        Region spacerH = new Region();
        HBox.setHgrow(spacerH, Priority.ALWAYS);

        // Badge estado
        Label lblEstado = new Label(estado);
        lblEstado.setStyle(
            "-fx-background-color: " + getBadgeColor(estado) + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 6 16 6 16;" +
            "-fx-background-radius: 20;"
        );

        header.getChildren().addAll(infoRuta, spacerH, lblEstado);

        // Body
        HBox body = new HBox(40);
        body.setAlignment(Pos.CENTER_LEFT);
        body.setPadding(new Insets(20, 32, 20, 32));

        // Fecha salida
        VBox boxFecha = infoCol(
            "📅  Fecha de salida",
            vuelo.getFechaHoraSalida().format(DT_FMT),
            pasado
        );

        // Total pagado
        VBox boxTotal = infoCol(
            "💳  Total pagado",
            "COP " + nf.format((long) total),
            pasado
        );

        // Lista de pasajeros / tiquetes
        VBox boxTiquetes = new VBox(8);
        Label lblTitTiq = new Label("🎫  Pasajeros (" + tiquetes.length + ")");
        lblTitTiq.setStyle(
            "-fx-font-size: 12px; -fx-text-fill: #888888; -fx-font-weight: bold;"
        );
        boxTiquetes.getChildren().add(lblTitTiq);

        for (Tiquete t : tiquetes) {
            HBox fila = new HBox(12);
            fila.setAlignment(Pos.CENTER_LEFT);

            Label lblNom = new Label(t.getNombrePasajero());
            lblNom.setStyle(
                "-fx-font-size: 15px; -fx-font-weight: bold;" +
                "-fx-text-fill: " + (pasado ? "#BDC3C7" : "#1E2A35") + ";"
            );

            Label lblAsiento = new Label("Asiento: " + t.getAsiento());
            lblAsiento.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: white;" +
                "-fx-background-color: #C9A84C;" +
                "-fx-padding: 3 10 3 10;" +
                "-fx-background-radius: 8;"
            );

            Label lblDoc = new Label(
                t.getTipoDocPasajero() + " " + t.getNumDocPasajero()
            );
            lblDoc.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");

            fila.getChildren().addAll(lblNom, lblAsiento, lblDoc);
            boxTiquetes.getChildren().add(fila);
        }

        body.getChildren().addAll(boxFecha, boxTotal, boxTiquetes);
        card.getChildren().addAll(header, body);
        return card;
    }

    private VBox infoCol(String titulo, String valor, boolean pasado) {
        VBox box = new VBox(6);
        box.setPrefWidth(260);

        Label lbl = new Label(titulo);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888; -fx-font-weight: bold;");

        Label val = new Label(valor);
        val.setStyle(
            "-fx-font-size: 18px; -fx-font-weight: bold;" +
            "-fx-text-fill: " + (pasado ? "#BDC3C7" : "#1E2A35") + ";"
        );

        box.getChildren().addAll(lbl, val);
        return box;
    }

    private String getBadgeColor(String estado) {
        switch (estado) {
            case "Confirmado": return "#27AE60";
            case "Programado": return "#2980B9";
            case "Atrasado":   return "#E67E22";
            case "Cancelado":  return "#C0392B";
            case "Terminado":  return "#7F8C8D";
            default:           return "#7F8C8D";
        }
    }

    private Label mensajeVacio(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle(
            "-fx-text-fill: #7F8C8D; -fx-font-size: 16px; -fx-padding: 8 0 0 0;"
        );
        return lbl;
    }

    private String abrev(String nombre) {
        if (nombre == null) return "N/A";
        return nombre.length() >= 3
                ? nombre.substring(0, 3).toUpperCase()
                : nombre.toUpperCase();
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
            ctrl.setUsuarioLogueado(clienteLogueado);

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