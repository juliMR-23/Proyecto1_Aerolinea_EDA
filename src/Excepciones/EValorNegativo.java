package principal;

public class EValorNegativo extends Exception {

    private static final long serialVersionUID = 1L;

    public EValorNegativo(String mensaje) {
        super(mensaje);
    }
}
