package util;

import excepciones.EValorNulo;

public class Valida {
    public static void validarTexto(String valor, String msg) throws EValorNulo {
        if (valor == null || valor.isBlank()) {
            throw new EValorNulo(msg);
        }
    }
}
