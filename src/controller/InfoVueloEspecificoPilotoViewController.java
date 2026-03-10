package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import principal.Aerolinea;
import principal.Aeropuerto;
import principal.Piloto;
import principal.Vuelo;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InfoVueloEspecificoPilotoViewController implements Initializable {

    @FXML private Label lblRutaTitulo;
    @FXML private Label lblEstado;
    @FXML private Label lblDuracion;
    @FXML private Label lblPuerta;
    @FXML private Label lblAvion;
    @FXML private Label lblMatricula;

    @FXML private VBox boxOrigenNombre;
    @FXML private VBox boxOrigenCiudad;
    @FXML private VBox boxOrigenPais;
    @FXML private VBox boxOrigenZona;
    @FXML private VBox boxOrigenSalida;

    @FXML private VBox boxDestinoNombre;
    @FXML private VBox boxDestinoCiudad;
    @FXML private VBox boxDestinoPais;
    @FXML private VBox boxDestinoZona;
    @FXML private VBox boxDestinoLlegada;

    @FXML private Button btnVolver;

    private static final DateTimeFormatter DT_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");

    private Piloto    pilotoLogueado;
    private Vuelo     vuelo;
    private Aerolinea aerolinea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public void setDatos(Vuelo vuelo, Piloto piloto) {
        this.vuelo          = vuelo;
        this.pilotoLogueado = piloto;

        Aeropuerto origen  = vuelo.getOrigen();
        Aeropuerto destino = vuelo.getDestino();
        String     estado  = vuelo.getEstadoVuelo();

        lblRutaTitulo.setText(
            origen.getNombre() + "  →  " + destino.getNombre() +
            "   |   " + vuelo.getFechaHoraSalida().format(DT_FMT)
        );

        lblEstado.setText(estado);
        lblEstado.setStyle(
            lblEstado.getStyle() +
            "-fx-background-color: " + getBadgeColor(estado) + ";"
        );

        int minutos = vuelo.calcularDuracion();
        lblDuracion.setText((minutos / 60) + "h " + (minutos % 60) + "m");

        String puerta = vuelo.getPuertaEmbarque();
        lblPuerta.setText(puerta != null ? puerta : "—");

        if (vuelo.getAvion() != null) {
            lblAvion.setText(vuelo.getAvion().getMarca() + " " + vuelo.getAvion().getModelo());
            lblMatricula.setText("Matrícula: " + vuelo.getAvion().getMatricula());
        } else {
            lblAvion.setText("—");
            lblMatricula.setText("");
        }

        // Origen
        llenarBox(boxOrigenNombre, "Aeropuerto",  origen.getNombre());
        llenarBox(boxOrigenCiudad, "Ciudad",       origen.getCiudad());
        llenarBox(boxOrigenPais,   "País",          origen.getPais());
        llenarBox(boxOrigenZona,   "Zona horaria", origen.getZonaHoraria());
        llenarBox(boxOrigenSalida, "📅  Salida",   vuelo.getFechaHoraSalida().format(DT_FMT));

        // Destino
        llenarBox(boxDestinoNombre,  "Aeropuerto",  destino.getNombre());
        llenarBox(boxDestinoCiudad,  "Ciudad",       destino.getCiudad());
        llenarBox(boxDestinoPais,    "País",          destino.getPais());
        llenarBox(boxDestinoZona,    "Zona horaria", destino.getZonaHoraria());
        String llegadaTxt = vuelo.getFechaHoraLlegada() != null
                ? vuelo.getFechaHoraLlegada().format(DT_FMT)
                : "—";
        llenarBox(boxDestinoLlegada, "🏁  Llegada", llegadaTxt);
    }

    private void llenarBox(VBox box, String titulo, String valor) {
        Label lbl = new Label(titulo);
        lbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888; -fx-font-weight: bold;");

        Label val = new Label(valor);
        val.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1E2A35;");

        box.getChildren().setAll(lbl, val);
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

    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Stage   stage = (Stage) btnVolver.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/MainPagePilotoView.fxml")
            );
            Parent root = loader.load();
            MainPagePilotoViewController ctrl = loader.getController();
            ctrl.setAerolinea(aerolinea);
            ctrl.setPiloto(pilotoLogueado);
            Scene scene = btnVolver.getScene();
            scene.setRoot(root);
            scene.getStylesheets().add(
                getClass().getResource("/css/app.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(maxim);
            stage.show();
        } catch (Exception e) {
            lblRutaTitulo.setText("⚠ Error al volver: " + e.getMessage());
            lblRutaTitulo.setStyle("-fx-text-fill: #C0392B;");
        }
    }
}