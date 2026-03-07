package principal;
import java.io.*;

public class Tiquete implements Serializable {
	String id;
	String asiento;
	double precio;
	Vuelo vuelo;
	String nombrePasajero;
	String numDocPasajero;
	String tipoDocPasajero;
	
	
	
	public Tiquete(String id, String asiento, double precio, Vuelo vuelo, String nombrePasajero,
			String numDocPasajero, String tipoDocPasajero) {
		this.id = id;
		this.asiento = asiento;
		this.precio = precio;
		this.vuelo = vuelo;
		this.nombrePasajero = nombrePasajero;
		this.numDocPasajero = numDocPasajero;
		this.tipoDocPasajero = tipoDocPasajero;
	}
	
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	public String getAsiento() {return asiento;}
	public void setAsiento(String asiento) {this.asiento = asiento;}
	public double getPrecio() {return precio;}
	public void setPrecio(double precio) {this.precio = precio;}
	public Vuelo getVuelo() {return vuelo;}
	public void setVuelo(Vuelo vuelo) {this.vuelo = vuelo;}
	public String getNombrePasajero() {return nombrePasajero;}
	public void setNombrePasajero(String nombrePasajero) {this.nombrePasajero = nombrePasajero;}
	public String getNumDocPasajero() {return numDocPasajero;}
	public void setNumDocPasajero(String numDocPasajero) {this.numDocPasajero = numDocPasajero;}
	public String getTipoDocPasajero() {return tipoDocPasajero;}
	public void setTipoDocPasajero(String tipoDocPasajero) {this.tipoDocPasajero = tipoDocPasajero;}
}
