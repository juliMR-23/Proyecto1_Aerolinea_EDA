package principal;

import java.util.Arrays;
import java.util.Date;

import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNegativo;
import excepciones.EValorNulo;



public class TripulanteCabina extends Empleado {
	private String[] idiomas;
	
	public TripulanteCabina(String nombre, String tipoDocumento, String documento, String telefono, String email,
			String password, double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia) throws EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail {
		super(nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, activo, aniosExperiencia);
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
	
}
