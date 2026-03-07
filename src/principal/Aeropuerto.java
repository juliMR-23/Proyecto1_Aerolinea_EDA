package principal;

public class Aeropuerto {

	private String id;
	private String nombre;
	private String ciudad;
	private String pais;
	private String zonaHoraria;

	public Aeropuerto(String id, String nombre, String ciudad, String pais, String codigoIATA, String zonaHoraria) {
		this.id = id;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.pais = pais;
		this.zonaHoraria = zonaHoraria;
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