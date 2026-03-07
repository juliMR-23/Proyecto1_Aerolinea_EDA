package principal;

import java.util.Date;

import excepciones.EPersonaInvalida;

public class Piloto extends Empleado {

	public Piloto(String id, String nombre, String tipoDocumento, String documento, String telefono, String email,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia, Aerolinea aerolinea)
			throws EPersonaInvalida {
		super(id, nombre, tipoDocumento, documento, telefono, email, salarioBase, fechaContratacion, activo, aniosExperiencia, aerolinea);
	}
	@Override
	public double calcularSalario() {
		return super.salarioBase;
	}
}
