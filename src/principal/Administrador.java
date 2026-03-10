package principal;

import java.io.Serializable;

import excepciones.EInvalidDocumento;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNulo;

public class Administrador extends Persona implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public Administrador(String nombre, String tipoDocumento, String documento, String telefono,String email, String password) throws EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {
		super(nombre, tipoDocumento, documento, telefono, email, password);
	}
	

}
