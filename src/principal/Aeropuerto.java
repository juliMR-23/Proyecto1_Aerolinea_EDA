package principal;

import util.IDAsign;

public class Aeropuerto {

	private String id;
	private String nombre;
	private String ciudad;
	private String pais;
	private String zonaHoraria;
	private static int cont = 0;

	public Aeropuerto(String nombre, String ciudad, String pais, String codigoIATA, String zonaHoraria) {
		this.id = IDAsign.asignar("AE", cont);
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.pais = pais;
		this.zonaHoraria = zonaHoraria;
		cont++;
	}

	public String getId() {
		return this.id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
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

	public void setZonaHoraria(String zonaHoraria) {
		this.zonaHoraria = zonaHoraria;
	}
}