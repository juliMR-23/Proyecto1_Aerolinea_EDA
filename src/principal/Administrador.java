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
import util.IDAsign;

public class Administrador extends Persona implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public Administrador(String nombre, String tipoDocumento, String documento, String telefono,String email, String password) throws EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {
		super(nombre, tipoDocumento, documento, telefono, email, password);
		this.id=IDAsign.asignar("AD",cont);
	}
	
	@Override
	public void copiarFicheroPersona(String dir) throws IOException {
		
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Administrador)this);
		b.close();
		f.close();
	}
	
	public static Administrador leerFicheroPersona(String dir) throws IOException, ClassNotFoundException {
		
		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Administrador administrador = (Administrador) b.readObject();
		b.close();
		f.close();
		return administrador;
	}
	

}
