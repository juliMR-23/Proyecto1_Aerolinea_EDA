package controller;

import javafx.collections.FXCollections;
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
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import principal.*;
import excepciones.*;

import java.net.URL;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class BuscarVuelosViewController implements Initializable {

    @FXML private Label                lblBienvenida;
    @FXML private MFXTextField         txtOrigen;
    @FXML private MFXTextField         txtDestino;
    @FXML private DatePicker           dpFechaIda;
    @FXML private MFXComboBox<Integer> cbPasajeros;
    @FXML private Button               btnBuscar;
    @FXML private Button               btnLogin;
    @FXML private Button               btnMisReservas;
    @FXML private VBox                 vboxResultados;

    private static final DateTimeFormatter HORA_FMT   = DateTimeFormatter.ofPattern("HH:mm");
    private static final double            PRECIO_BASE = 250000.0;

    private Cliente clienteLogueado = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbPasajeros.setItems(FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9));
        cbPasajeros.selectFirst();

        dpFechaIda.setValue(LocalDate.now());
        dpFechaIda.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #3a3a3a; -fx-text-fill: #666666;");
                }
            }
        });

        btnBuscar.setOnAction(arg0 -> {
			try {
				handleBuscar(arg0);
			} catch (EPilotosInsuficientes e) {
				e.printStackTrace();
			}
		});
    }

    public void setUsuarioLogueado(Cliente cliente) {
        this.clienteLogueado = cliente;
        lblBienvenida.setText("Bienvenido, " + cliente.getNombre() + "  ✈"); 
        btnLogin.setText("Cerrar Sesión  ✕");
        btnLogin.setStyle(
            "-fx-background-color: #C0392B; -fx-text-fill: white;" +
            "-fx-font-size: 15px; -fx-font-weight: bold;" +
            "-fx-background-radius: 10; -fx-cursor: hand; -fx-border-color: transparent;"
        );
        btnMisReservas.setVisible(true);
        btnMisReservas.setManaged(true);
    }

    @FXML
    public void handleLoginLogout(ActionEvent event) {
        if (clienteLogueado == null) {
            navegarLogin(); 
        } else {
            clienteLogueado = null;
            lblBienvenida.setText("Bienvenido a EDArolinea ✈");
            btnLogin.setText("Iniciar Sesión  →");
            btnLogin.setStyle(
                "-fx-background-color: #C9A84C; -fx-text-fill: #1E2A35;" +
                "-fx-font-size: 15px; -fx-font-weight: bold;" +
                "-fx-background-radius: 10; -fx-cursor: hand; -fx-border-color: transparent;"
            );
            btnMisReservas.setVisible(false);
            btnMisReservas.setManaged(false);
            vboxResultados.getChildren().clear();
        }
    }

    
    @FXML
    public void handleMisReservas(ActionEvent event) {
        if (clienteLogueado == null) return;
        try {
            Stage stage   = (Stage) btnLogin.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/VerReservasView.fxml")
            );
            Parent root = loader.load();
            VerReservasViewController ctrl = loader.getController();
            ctrl.setCliente(clienteLogueado);
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
    

    private void handleBuscar(ActionEvent event) throws EPilotosInsuficientes {
    	//TODO: Implementar clases y excepciones
        String origenTxt  = txtOrigen.getText().trim();
        String destinoTxt = txtDestino.getText().trim();
        LocalDate fecha   = dpFechaIda.getValue();
        int pasajeros     = cbPasajeros.getValue();

        vboxResultados.getChildren().clear();

        if (origenTxt.isEmpty() || destinoTxt.isEmpty()) {
            agregarMensaje("⚠ Por favor ingresa la ciudad de origen y destino.", "#C0392B");
            return;
        }
        if (fecha == null) {
            agregarMensaje("⚠ Por favor selecciona la fecha de ida.", "#C0392B"); 
            return;
        }

        Vuelo[] vuelos = buscarVuelos(origenTxt, destinoTxt, fecha);

        Label titulo = new Label(
            vuelos.length + " vuelo(s) encontrado(s): " + origenTxt +
            " → " + destinoTxt + " | " + fecha + " | " + pasajeros + " pasajero(s)"
        );
        titulo.setStyle("-fx-text-fill: #ECF0F1; -fx-font-size: 16px; -fx-padding: 6 0 4 0;");
        vboxResultados.getChildren().add(titulo);

        if (vuelos.length == 0) {
            agregarMensaje("No se encontraron vuelos para esa ruta.", "#C0392B");
        } else {
            for (Vuelo v : vuelos) {
                try {
                    vboxResultados.getChildren().add(crearTarjetaVuelo(v, pasajeros));
                } catch (Exception e) {
                    System.err.println("ERROR tarjeta: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private HBox crearTarjetaVuelo(Vuelo vuelo, int pasajeros) {
        LocalDateTime salida  = vuelo.getFechaHoraSalida();
        LocalDateTime llegada = vuelo.getFechaHoraLlegada() != null
                ? vuelo.getFechaHoraLlegada()
                : salida.plusMinutes(75);

        Duration dur    = Duration.between(salida, llegada); //TODO: Sacar de fichero
        String durStr   = dur.toHours() + "h " + dur.toMinutesPart() + "m";
        String codO     = abrev(vuelo.getOrigen().getNombre());
        String codD     = abrev(vuelo.getDestino().getNombre());
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "CO"));

        HBox card = new HBox(24);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(24, 32, 24, 32));
        card.setStyle(
            "-fx-background-color: #FFFFFF;" +
            "-fx-background-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 12, 0, 3, 3);"
        );

        // Salida
        VBox salidaBox = new VBox(4);
        salidaBox.setAlignment(Pos.CENTER_LEFT);
        salidaBox.setPrefWidth(110);
        Label lblSalida = new Label(salida.format(HORA_FMT));
        lblSalida.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1E2A35;");
        Label lblCodO = new Label(codO);
        lblCodO.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-weight: bold;");
        salidaBox.getChildren().addAll(lblSalida, lblCodO);

        // Ruta
        VBox rutaBox = new VBox(6);
        rutaBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(rutaBox, Priority.ALWAYS);
        Label lblDur = new Label(durStr);
        lblDur.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");
        HBox lineaBox = new HBox();
        lineaBox.setAlignment(Pos.CENTER);
        Label p1 = new Label("●");
        p1.setStyle("-fx-text-fill: #1E2A35; -fx-font-size: 9px;");
        Region l1 = new Region();
        l1.setMinHeight(1);
        l1.setStyle("-fx-border-color: #AAAAAA; -fx-border-width: 1 0 0 0; -fx-border-style: dashed;");
        HBox.setHgrow(l1, Priority.ALWAYS);
        Label avion = new Label("✈");
        avion.setStyle("-fx-text-fill: #1E2A35; -fx-font-size: 20px; -fx-padding: 0 6 0 6;");
        Region l2 = new Region();
        l2.setMinHeight(1);
        l2.setStyle("-fx-border-color: #AAAAAA; -fx-border-width: 1 0 0 0; -fx-border-style: dashed;");
        HBox.setHgrow(l2, Priority.ALWAYS);
        Label p2 = new Label("●");
        p2.setStyle("-fx-text-fill: #1E2A35; -fx-font-size: 9px;");
        lineaBox.getChildren().addAll(p1, l1, avion, l2, p2);
        rutaBox.getChildren().addAll(lblDur, lineaBox);

        // Llegada
        VBox llegadaBox = new VBox(4);
        llegadaBox.setAlignment(Pos.CENTER_LEFT);
        llegadaBox.setPrefWidth(110);
        Label lblLlegada = new Label(llegada.format(HORA_FMT));
        lblLlegada.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1E2A35;");
        Label lblCodD = new Label(codD);
        lblCodD.setStyle("-fx-font-size: 14px; -fx-text-fill: #666666; -fx-font-weight: bold;");
        llegadaBox.getChildren().addAll(lblLlegada, lblCodD);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Precio
        VBox precioBox = new VBox(2);
        precioBox.setAlignment(Pos.CENTER_RIGHT);
        precioBox.setPrefWidth(200);
        Label lblDesde = new Label("Desde");
        lblDesde.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888;");
        Label lblPrecio = new Label("COP " + nf.format((long) PRECIO_BASE));
        lblPrecio.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1E2A35;");
        precioBox.getChildren().addAll(lblDesde, lblPrecio);

        Button btnSel = new Button("Seleccionar");
        btnSel.setStyle(
            "-fx-background-color: #C9A84C; -fx-text-fill: #1E2A35;" +
            "-fx-font-size: 15px; -fx-font-weight: bold;" +
            "-fx-background-radius: 10; -fx-cursor: hand;" +
            "-fx-padding: 12 28 12 28; -fx-border-color: transparent;"
        );
        btnSel.setOnAction(e -> handleSeleccionarVuelo(vuelo, pasajeros));

        card.getChildren().addAll(salidaBox, rutaBox, llegadaBox, spacer, precioBox, btnSel);
        return card;
    }

    private void handleSeleccionarVuelo(Vuelo vuelo, int pasajeros) {
    	//TODO: Implementar excepciones
        if (clienteLogueado == null) {
            agregarMensaje("⚠ Debes iniciar sesión para comprar un tiquete.", "#C0392B");
            return;
        }
        try {
            Stage stage   = (Stage) btnLogin.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/ComprarVueloView.fxml")
            );
            Parent root = loader.load();
            ComprarVueloViewController ctrl = loader.getController();
            ctrl.setDatos(vuelo, pasajeros, clienteLogueado);
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

   
    private Vuelo[] buscarVuelos(String origenTxt, String destinoTxt, LocalDate fecha) {
        try {
            Aeropuerto aeroO = new Aeropuerto("Olaya Herrera", origenTxt,
                    "Colombia", "America/Bogota", -75.4231, 6.1644);
            Aeropuerto aeroD = new Aeropuerto("El Dorado", destinoTxt,
                    "Colombia", "America/Bogota", -74.1469, 4.7014);

            Avion avion = new Avion("AV-001", "Airbus", "A320", 180, true, 850.0);

            TripulanteCabina tripDummy = new TripulanteCabina(
                    "Tripulante Demo", "CC", "5555555", "3155555555",
                    "demo@trip.com", "Pass123",
                    4000000.0, new java.util.Date(), true, 2
            );
            TripulanteCabina[] trip = new TripulanteCabina[]{ tripDummy };

            Piloto p1 = new Piloto("Piloto Uno", "CC", "6666661", "3166666661",
                    "p1@demo.com", "Pass123", 7000000.0, new java.util.Date(), true, 4);
            Piloto p2 = new Piloto("Piloto Dos", "CC", "6666662", "3166666662",
                    "p2@demo.com", "Pass123", 7000000.0, new java.util.Date(), true, 4);
            Piloto[] pilotos = new Piloto[]{ p1, p2 };

            Vuelo[] vuelos = new Vuelo[3];
            vuelos[0] = new Vuelo(aeroO, aeroD, fecha.atTime(7,  0),  avion, trip, pilotos);
            vuelos[1] = new Vuelo(aeroO, aeroD, fecha.atTime(12, 30), avion, trip, pilotos);
            vuelos[2] = new Vuelo(aeroO, aeroD, fecha.atTime(17, 45), avion, trip, pilotos);

            return vuelos;

        } catch (Exception e) {
            System.err.println("ERROR creando vuelos: " + e.getMessage());
            e.printStackTrace();
            return new Vuelo[0];
        }
    }


    private String abrev(String nombre) {
        if (nombre == null) return "N/A";
        return nombre.length() >= 3
                ? nombre.substring(0, 3).toUpperCase()
                : nombre.toUpperCase();
    }

    private void agregarMensaje(String msg, String color) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 18px; -fx-padding: 8 0 0 0;");
        vboxResultados.getChildren().add(lbl);
    }

    private void navegarLogin() {
        try {
            Stage stage   = (Stage) btnLogin.getScene().getWindow();
            boolean maxim = stage.isMaximized();
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/view/LoginView.fxml")
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