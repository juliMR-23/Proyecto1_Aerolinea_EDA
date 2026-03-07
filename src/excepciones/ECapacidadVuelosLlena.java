package excepciones;

public class ECapacidadVuelosLlena extends Exception {

    private static final long serialVersionUID = 1L;

    public ECapacidadVuelosLlena(String mensaje) {
        super(mensaje);
    }
}
