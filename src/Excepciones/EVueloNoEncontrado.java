package principal;

public class EVueloNoEncontrado extends Exception {

    private static final long serialVersionUID = 1L;

    public EVueloNoEncontrado(String mensaje) {
        super(mensaje);
    }
}