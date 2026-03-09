package principal;
import java.io.*;

import excepciones.EValorNegativo;
import excepciones.EValorNulo;
import util.IDAsign;
import util.Valida;

public class Tiquete implements Serializable {
	private String id;
	private String asiento;
	private double precio;
	private Vuelo vuelo;
	private String nombrePasajero;
	private String numDocPasajero;
	private String tipoDocPasajero;
	private static int cont = 0;
	
	
	private static final long serialVersionUID = 1L;
	
	public Tiquete(String id, String asiento, Vuelo vuelo, String nombrePasajero,
			String numDocPasajero, String tipoDocPasajero) throws EValorNulo, EValorNegativo {
		
		Valida.validarTexto(asiento, "El asiento no puede ser vacío");
		Valida.validarTexto(nombrePasajero, "El nombre del pasajero no puede ser vacío");
		// Soy Sofia Soto
		Valida.validarTexto(numDocPasajero, "El tipo de documento del pasajero no puede ser vacío");
		Valida.validarTexto(tipoDocPasajero, "El número de documento del pasajero no puede ser vacío");
		if(vuelo == null) {throw new EValorNulo("El vuelo no puede estar vacío");}
		
		this.id = IDAsign.asignar("TI",cont);
		
		this.asiento = asiento;
		this.precio = vuelo.getPrecio();
		this.vuelo = vuelo;
		this.nombrePasajero = nombrePasajero;
		this.numDocPasajero = numDocPasajero;
		this.tipoDocPasajero = tipoDocPasajero;
		cont++;
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
	
	public void copiarFicheroTiquete(String dir) throws IOException {
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Tiquete)this);
		f.close();
		b.close();
	}
	public static Tiquete leerFicherTiquete(String dir) throws IOException, ClassNotFoundException {
		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Tiquete tiquete = (Tiquete) b.readObject();
		f.close();
		b.close();
		return tiquete;
	}
	

}
