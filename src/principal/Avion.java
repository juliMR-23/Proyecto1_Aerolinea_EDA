package principal;

public class Avion {
	private String matricula;
	private String marca;
	private String modelo;
	private int capacidad;//cantidad de pasajeros que puede llevar
	private boolean disponible;
	
	public Avion(String matricula, String marca, String modelo, int capacidad, boolean disponible) {
		this.matricula = matricula;
		this.marca = marca;
		this.modelo = modelo;
		this.capacidad = capacidad;
		this.disponible = disponible;
	}
	
	public String getMatricula() {
		return matricula;
	}
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public boolean isDisponible() {
		return disponible;
	}
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
}
