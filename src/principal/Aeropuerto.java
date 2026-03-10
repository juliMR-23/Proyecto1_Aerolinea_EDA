package principal;

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
	private boolean isActive;


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
		this.isActive=true;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
}