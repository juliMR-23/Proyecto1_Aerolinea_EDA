package principal;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.BuscarVuelosViewController;
import excepciones.EValorNulo;

public class MainApp extends Application {

    private Aerolinea aerolinea;

    @Override
    public void start(Stage primaryStage) throws Exception {

        aerolinea = new Aerolinea("EDAerolinea");
        cargarDatos();
        crearAdmin();

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

        primaryStage.setTitle("EDAerolinea");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(e -> guardarDatos());
        primaryStage.show();
    }

    private void cargarDatos() throws EValorNulo {
        String[] errores = aerolinea.cargarTodo();
        aerolinea.contInicio();

        for (String e : errores)
            System.err.println("⚠ [ADVERTENCIA] " + e);
    }

    private void guardarDatos() {
        try {
            aerolinea.guardarTodo();
        } catch (Exception e) {
            System.err.println("⚠ Error al guardar: " + e.getMessage());
        }
    }
    
    private void crearAdmin() {
        if (aerolinea.listAdministradoresActivos().length == 0) {
            try {
                aerolinea.addAdministrador(
                    "Admin EDAerolinea",
                    "CC",
                    "5555555",
                    "3785263214",
                    "admin@edaerolinea.com",
                    "Admin123"
                );
                aerolinea.guardarAdministradores();
                System.out.println("[OK] Admin por defecto creado: admin@edaerolinea.com / Admin123");
            } catch (Exception e) {
                System.err.println("[ERROR] No se pudo crear el admin por defecto: " + e.getMessage());
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}