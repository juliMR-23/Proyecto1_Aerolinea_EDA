package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import excepciones.*;


public class TripulanteCabina extends Empleado implements Serializable{
	private String[] idiomas;
	private static final long serialVersionUID = 1L;
	
	public TripulanteCabina(String nombre, String tipoDocumento, String documento, String telefono, String email,
			String password, double salarioBase, Date fechaContratacion, int aniosExperiencia) throws EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento, EInvalidName {
		super(nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, aniosExperiencia);
		idiomas=new String[0];
	}

	@Override
	public double calcularSalario() {
		return super.salarioBase + 10*idiomas.length + 100*super.aniosExperiencia;
	}

	public void addIdioma(String idioma) throws EValorNulo {
		if(idioma.isBlank())
			throw new EValorNulo("El idioma no puede estar vacío");
		if(indexIdioma(idioma)==-1) {//para no guardar repetidos
			idiomas = Arrays.copyOf(idiomas, idiomas.length+1);
			idiomas[idiomas.length-1] = idioma;
		}
	}
	public int indexIdioma(String idioma) {
		int n=0;
		while(n<idiomas.length && !idiomas[n].equalsIgnoreCase(idioma)) {
			n++;
		}
		if(n<idiomas.length)
			return n;
		return -1;//si no lo encuentra retorna -1
	}
	public void deleteIdioma(String idioma) {
		int n = indexIdioma(idioma);
		if(n!=-1) {//el idioma existe
			idiomas[n]=idiomas[idiomas.length-1];//reemplaza el idioma a borrar para que no queden "huecos" en el array
			idiomas = Arrays.copyOf(idiomas, idiomas.length-1);//reduce el arreglo (elimina última pos)
		}
	}
	public String[] listIdiomas() {
		return idiomas;
	}
	public void setIdiomas(String[] idiomas) {
		this.idiomas = idiomas;
	}

    public void wFicheroTripulante(String dir) throws IOException {

        FileOutputStream f = new FileOutputStream(dir);
        ObjectOutputStream b = new ObjectOutputStream(f);
        b.writeObject((TripulanteCabina)this);
        b.close();
        f.close();
    }

    public static TripulanteCabina rFicheroPersona(String dir) throws IOException, ClassNotFoundException {

        FileInputStream f = new FileInputStream(dir);
        ObjectInputStream b = new ObjectInputStream(f);
        TripulanteCabina tp = (TripulanteCabina) b.readObject();
        b.close();
        f.close();
        return tp;
    }
	
}
