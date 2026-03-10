package excepciones;

public class EEmpleadosInsuficientes extends Exception{
	public EEmpleadosInsuficientes(String tipo) {
		super("Cantidad mínima de"+ tipo+" no alcanzada");
	}
}
