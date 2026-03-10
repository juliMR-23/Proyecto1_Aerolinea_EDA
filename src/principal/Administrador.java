package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import excepciones.EInvalidDocumento;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNulo;
import util.Valida;

public class Administrador extends Persona implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public Administrador(String nombre, String tipoDocumento, String documento, String telefono,String email, String password) throws EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {
		super(nombre, tipoDocumento, documento, telefono, email, password);
	}
	
	public void copiarFicheroAdministrador(String dir) throws IOException, EValorNulo {
	    Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileOutputStream f = new FileOutputStream(dir);
	    ObjectOutputStream b = new ObjectOutputStream(f);
	    b.writeObject((Administrador)this);
	    b.close();
	    f.close();
	}

	public static Administrador leerFicheroAdministrador(String dir) throws IOException, ClassNotFoundException, EValorNulo {
	    Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Administrador admin = (Administrador) b.readObject();
	    b.close();
	    f.close();
	    return admin;
	}
}
