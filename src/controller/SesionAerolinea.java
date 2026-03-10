package controller;

public class SesionAerolinea {

    private static Aerolinea aerolinea;

    public static Aerolinea getAerolinea() {
        return aerolinea;
    }

    public static void setAerolinea(Aerolinea a) {
        aerolinea = a;
    }
}
