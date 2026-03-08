package principal;

import excepciones.EInvalidPass;
import excepciones.EValorNulo;

public class Administrador extends Persona{

	public Administrador(String id, String nombre, String tipoDocumento, String documento, String telefono,String email, String password) throws EValorNulo, EInvalidPass {
		super(id, nombre, tipoDocumento, documento, telefono, email, password);
	}
	
	//métodos especiales del admin

}
