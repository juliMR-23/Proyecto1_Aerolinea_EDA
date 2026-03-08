package excepciones;

public class EPilotosInsuficientes extends Exception{
	public EPilotosInsuficientes() {
		super("Cantidad mínima de pilotos no alcanzada");
	}
}
