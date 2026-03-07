package excepciones;

public class EVueloYaAsignado extends Exception {

    private static final long serialVersionUID = 1L;

    public EVueloYaAsignado(String mensaje) {
        super(mensaje);
    }
}
