package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import excepciones.EInvalidDocumento;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNegativo;
import excepciones.EValorNulo;
import util.Valida;

public class Piloto extends Empleado implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public Piloto(String nombre, String tipoDocumento, String documento, String telefono, String email, String password,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia) throws EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {
		super(nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, activo, aniosExperiencia);
	}
	@Override
	public double calcularSalario() {
		return super.salarioBase + 200*super.aniosExperiencia + 500;
	}
	
	public void copiarFicheroPiloto(String dir) throws IOException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Piloto)this);
		b.close();
		f.close();
	}
	public static Piloto leerFicheroPiloto(String dir) throws IOException, ClassNotFoundException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Piloto piloto = (Piloto) b.readObject();
		b.close();
		f.close();
		return piloto;
	}
}
