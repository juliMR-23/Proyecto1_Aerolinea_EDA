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
import principal.Aerolinea;
import principal.Piloto;
import principal.Vuelo;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainPagePilotoViewController implements Initializable {

    @FXML private Label  lblSubtitulo;
    @FXML private Label  lblTotalVuelosMes;
    @FXML private Label  lblHorasAcumuladas;
    @FXML private Label  lblAniosExp;
    @FXML private Label  lblSalario;
    @FXML private Label  lblMesAnio;
    @FXML private VBox   vboxInformeMensual;
    @FXML private Button btnProximoVuelo;
    @FXML private Button btnCerrarSesion;

    private static final DateTimeFormatter DT_FMT  =
            DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");
    private static final DateTimeFormatter MES_FMT =
            DateTimeFormatter.ofPattern("MMMM yyyy", Locale.of("es", "CO"));

    private Piloto    pilotoLogueado;
    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public void setPiloto(Piloto piloto) {
        this.pilotoLogueado = piloto;

        NumberFormat nf = NumberFormat.getInstance(Locale.of("es", "CO"));

        lblSubtitulo.setText(
            "Bienvenido, " + piloto.getNombre() + "  —  Licencia activa"
        );
        lblHorasAcumuladas.setText((int) piloto.getHorasVueloAcumuladas() + "h");
        lblAniosExp.setText(String.valueOf(piloto.getAniosExperiencia()));
        lblSalario.setText("COP " + nf.format((long) piloto.calcularSalario()));
        lblMesAnio.setText(LocalDate.now().format(MES_FMT));

        cargarInformeMensual();
    }

    private void cargarInformeMensual() {
        vboxInformeMensual.getChildren().clear();

        Vuelo[]       vuelos     = pilotoLogueado.getVuelosAsignados();
        LocalDateTime ahora      = LocalDateTime.now();
        int           mesActual  = ahora.getMonthValue();
        int           anioActual = ahora.getYear();
        int           contMes    = 0;

        for (int i = 0; i < pilotoLogueado.getCantidadVuelos(); i++) {
            Vuelo v = vuelos[i];
            if (v == null) continue;

            LocalDateTime salida = v.getFechaHoraSalida();
            if (salida.getMonthValue() == mesActual && salida.getYear() == anioActual) {
                vboxInformeMensual.getChildren().add(crearFilaVuelo(v, ahora));
                contMes++;
            }
        }

        lblTotalVuelosMes.setText(String.valueOf(contMes));

        if (contMes == 0) {
            Label lbl = new Label("No tienes vuelos asignados este mes.");
            lbl.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 16px; -fx-padding: 8 0 0 0;");
            vboxInformeMensual.getChildren().add(lbl);
        }
    }

    private HBox crearFilaVuelo(Vuelo vuelo, LocalDateTime ahora) {
        boolean pasado     = vuelo.getFechaHoraSalida().isBefore(ahora);
        String  estado     = vuelo.getEstadoVuelo();
        String  colorTexto = pasado ? "#BDC3C7" : "#1E2A35";

        HBox card = new HBox(24);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20, 32, 20, 32));
        card.setStyle(
            "-fx-background-color: " + (pasado ? "#243342" : "#FFFFFF") + ";" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 3, 3);"
        );

        // Ruta
        VBox boxRuta = new VBox(4);
        boxRuta.setPrefWidth(420);
        Label lblRuta = new Label(
            vuelo.getOrigen().getCiudad()  + " (" + abrev(vuelo.getOrigen().getNombre())  + ")" +
            "  →  " +
            vuelo.getDestino().getCiudad() + " (" + abrev(vuelo.getDestino().getNombre()) + ")"
        );
        lblRuta.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #C9A84C;");
        Label lblId = new Label("Vuelo ID: " + vuelo.getId());
        lblId.setStyle("-fx-font-size: 12px; -fx-text-fill: #7F8C8D;");
        boxRuta.getChildren().addAll(lblRuta, lblId);

        // Fecha salida
        VBox boxFecha = infoCol("📅  Salida", vuelo.getFechaHoraSalida().format(DT_FMT), colorTexto);

        // Llegada
        String llegadaTxt = vuelo.getFechaHoraLlegada() != null
                ? vuelo.getFechaHoraLlegada().format(DT_FMT)
                : "—";
        VBox boxLlegada = infoCol("🏁  Llegada", llegadaTxt, colorTexto);

        // Avión
        VBox boxAvion = infoCol(
            "✈  Avión",
            vuelo.getAvion() != null ? vuelo.getAvion().getModelo() : "—",
            colorTexto
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

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

        card.getChildren().addAll(boxRuta, boxFecha, boxLlegada, boxAvion, spacer, lblEstado);
        return card;
    }

    private VBox infoCol(String titulo, String valor, String colorValor) {
        VBox box = new VBox(4);
        box.setPrefWidth(220);
        Label lbl = new Label(titulo);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888; -fx-font-weight: bold;");
        Label val = new Label(valor);
        val.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + colorValor + ";");
        box.getChildren().addAll(lbl, val);
        return box;
    }

    private Vuelo buscarProximoVuelo() {
        Vuelo[]       vuelos  = pilotoLogueado.getVuelosAsignados();
        LocalDateTime ahora   = LocalDateTime.now();
        Vuelo         proximo = null;

        for (int i = 0; i < pilotoLogueado.getCantidadVuelos(); i++) {
            Vuelo v = vuelos[i];
            if (v == null) continue;
            if (v.getFechaHoraSalida().isAfter(ahora)) {
                if (proximo == null ||
                    v.getFechaHoraSalida().isBefore(proximo.getFechaHoraSalida())) {
                    proximo = v;
                }
            }
        }
        return proximo;
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

    private String abrev(String nombre) {
        if (nombre == null) return "N/A";
        return nombre.length() >= 3
                ? nombre.substring(0, 3).toUpperCase()
                : nombre.toUpperCase();
    }

    @FXML
    public void handleVerProximoVuelo(ActionEvent event) {
        Vuelo proximo = buscarProximoVuelo();
        if (proximo == null) {
            btnProximoVuelo.setText("No tienes vuelos próximos");
            btnProximoVuelo.setDisable(true);
            return;
        }
        try {
            Stage   stage = (Stage) btnProximoVuelo.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/InfoVueloEspecificoPilotoView.fxml")
            );
            Parent root = loader.load();
            InfoVueloEspecificoPilotoViewController ctrl = loader.getController();
            ctrl.setDatos(proximo, pilotoLogueado);
            ctrl.setAerolinea(aerolinea);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            lblSubtitulo.setText("⚠ Error al abrir el vuelo: " + e.getMessage());
            lblSubtitulo.setStyle("-fx-text-fill: #C0392B;");
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        try {
            Stage   stage = (Stage) btnCerrarSesion.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/BuscarVuelosView.fxml")
            );
            Parent root = loader.load();
            BuscarVuelosViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            lblSubtitulo.setText("⚠ Error al cerrar sesión: " + e.getMessage());
            lblSubtitulo.setStyle("-fx-text-fill: #C0392B;");
        }
    }
}