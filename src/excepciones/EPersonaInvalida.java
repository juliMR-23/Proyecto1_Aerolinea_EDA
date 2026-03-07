package excepciones;

public class EPersonaInvalida extends Exception {

    private static final long serialVersionUID = 1L;

    public EPersonaInvalida(String mensaje) {
        super(mensaje);
    }
}
