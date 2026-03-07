package excepciones;

public class EClienteInvalido extends Exception {

    private static final long serialVersionUID = 1L;

    public EClienteInvalido(String mensaje) {
        super(mensaje);
    }
}
