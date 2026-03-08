package principal;

import java.util.Date;

import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNegativo;
import excepciones.EValorNulo;

public class Piloto extends Empleado {

	public Piloto(String id, String nombre, String tipoDocumento, String documento, String telefono, String email, String password,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia) throws EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail {
		super(id, nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, activo, aniosExperiencia);
	}
	@Override
	public double calcularSalario() {
		return super.salarioBase + 200*super.aniosExperiencia + 500;
	}
}
