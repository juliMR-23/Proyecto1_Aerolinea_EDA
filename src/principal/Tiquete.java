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
	
	private static final long serialVersionUID = 1L;
	
	public Tiquete(String asiento, Vuelo vuelo, String nombrePasajero,
			String numDocPasajero, String tipoDocPasajero) throws EValorNulo{
		
		Valida.validarTexto(asiento, "El asiento no puede ser vacío");
		Valida.validarTexto(nombrePasajero, "El nombre del pasajero no puede ser vacío");
		Valida.validarTexto(numDocPasajero, "El número de documento del pasajero no puede ser vacío");
		Valida.validarTexto(tipoDocPasajero, "El tipo de documento del pasajero no puede ser vacío");
		if(vuelo == null) {throw new EValorNulo("El vuelo no puede estar vacío");}
		
		this.id = IDAsign.asignar("TI",Aerolinea.getCont());
		
		this.asiento = asiento;
		this.precio = vuelo.getPrecio();
		this.vuelo = vuelo;
		this.nombrePasajero = nombrePasajero;
		this.numDocPasajero = numDocPasajero;
		this.tipoDocPasajero = tipoDocPasajero;
		Aerolinea.aumentaCont();
	}
	
	public String getId() {return id;}
	public void setId(String id) throws EValorNulo {
		Valida.validarTexto(id, "El id no puede ser vacío");
		this.id = id;
	}
	public String getAsiento() {return asiento;}
	public void setAsiento(String asiento) throws EValorNulo {
		Valida.validarTexto(asiento, "El asiento no puede ser vacío");
		this.asiento = asiento;
	}
	public double getPrecio() {return precio;}
	public void setPrecio(double precio) throws EValorNegativo {
		if(precio<=0) throw new EValorNegativo("El precio debe ser mayor a cero");
		this.precio = precio;
	}
	public Vuelo getVuelo() {return vuelo;}
	public void setVuelo(Vuelo vuelo) throws EValorNulo {
		if(vuelo == null) throw new EValorNulo("El vuelo no puede estar vacío");
		this.vuelo = vuelo;
	}
	public String getNombrePasajero() {return nombrePasajero;}
	public void setNombrePasajero(String nombrePasajero) throws EValorNulo {
		Valida.validarTexto(nombrePasajero, "El nombre del pasajero no puede ser vacío");
		this.nombrePasajero = nombrePasajero;	
	}
	public String getNumDocPasajero() {return numDocPasajero;}
	public void setNumDocPasajero(String numDocPasajero) throws EValorNulo {
		Valida.validarTexto(numDocPasajero, "El número de documento del pasajero no puede ser vacío");
		this.numDocPasajero = numDocPasajero;	
	}
	public String getTipoDocPasajero() {return tipoDocPasajero;}
	public void setTipoDocPasajero(String tipoDocPasajero) throws EValorNulo {
		Valida.validarTexto(tipoDocPasajero, "El tipo de documento del pasajero no puede ser vacío");
		this.tipoDocPasajero = tipoDocPasajero;	
	}
	
	public void copiarFicheroTiquete(String dir) throws IOException {
		
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Tiquete)this);
		b.close();
		f.close();
	}
	public static Tiquete leerFicheroTiquete(String dir) throws IOException, ClassNotFoundException {

		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Tiquete tiquete = (Tiquete) b.readObject();
		b.close();
		f.close();
		return tiquete;
	}

}
