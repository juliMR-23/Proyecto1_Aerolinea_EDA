package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import excepciones.EValorNulo;
import util.Valida;
import util.IDAsign;

public class Aeropuerto implements Serializable{

	private String id;
	private String nombre;
	private String ciudad;
	private String pais;
	private String zonaHoraria;
	private double longitud;
	private double latitud;
	private static final long serialVersionUID = 1L;


	public Aeropuerto(String nombre, String ciudad, String pais, String zonaHoraria, double longitud, double latitud) throws EValorNulo {
		Valida.validarTexto(nombre, "El nombre no puede estar vacío");
		Valida.validarTexto(ciudad, "La ciudad no puede estar vacía");
		Valida.validarTexto(pais, "El país no puede estar vacío");
		Valida.validarTexto(zonaHoraria, "La zona horaria no puede estar vacía");
		
		this.id = IDAsign.asignar("AE", Aerolinea.getCont());
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.pais = pais;
		this.zonaHoraria = zonaHoraria;
		this.longitud = longitud;
		this.latitud = latitud;
		Aerolinea.aumentaCont();
	}

	public String getId() {
		return this.id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) throws EValorNulo {
		Valida.validarTexto(nombre, "El nombre no puede estar vacío");
		this.nombre = nombre;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public String getPais() {
		return this.pais;
	}

	public String getZonaHoraria() {
		return this.zonaHoraria;
	}

	public void setZonaHoraria(String zonaHoraria) throws EValorNulo {
		Valida.validarTexto(zonaHoraria, "La zona horaria no puede estar vacía");
		this.zonaHoraria = zonaHoraria;
	}

	public double getLongitud() {
		return longitud;
	}

	public double getLatitud() {
		return latitud;
	}
	
	public void copiarFicheroAeropuerto(String dir) throws IOException, EValorNulo {
	    Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileOutputStream f = new FileOutputStream(dir);
	    ObjectOutputStream b = new ObjectOutputStream(f);
	    b.writeObject((Aeropuerto)this);
	    b.close();
	    f.close();
	}

	public static Aeropuerto leerFicheroAeropuerto(String dir) throws IOException, ClassNotFoundException, EValorNulo {
	    Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Aeropuerto aeropuerto = (Aeropuerto) b.readObject();
	    b.close();
	    f.close();
	    return aeropuerto;
	}
}