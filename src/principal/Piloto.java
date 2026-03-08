package principal;

import java.util.Date;

import excepciones.EValorNegativo;
import excepciones.EValorNulo;

public class Piloto extends Empleado {

	public Piloto(String id, String nombre, String tipoDocumento, String documento, String telefono, String email,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia) throws EValorNulo, EValorNegativo {
		super(id, nombre, tipoDocumento, documento, telefono, email, salarioBase, fechaContratacion, activo, aniosExperiencia);
	}
	@Override
	public double calcularSalario() {
		return super.salarioBase + 200*super.aniosExperiencia + 500;
	}
}
