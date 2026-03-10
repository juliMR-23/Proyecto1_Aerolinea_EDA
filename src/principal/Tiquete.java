package principal;
import java.io.*;

import excepciones.EInvalidDocumento;
import excepciones.EInvalidName;
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
	private boolean isActive;
	
	private static final long serialVersionUID = 1L;
	
	public Tiquete(String asiento, Vuelo vuelo, String nombrePasajero,
			String numDocPasajero, String tipoDocPasajero) throws EValorNulo, EInvalidDocumento, EInvalidName{
		
		Valida.validarTexto(asiento, "El asiento no puede ser vacío");
		validarNombre(nombrePasajero);
		validarDocumento(numDocPasajero);
		validarTipoDoc(tipoDocPasajero);
		if(vuelo == null) {throw new EValorNulo("El vuelo no puede estar vacío");}
		
		this.id = IDAsign.asignar("TI",Aerolinea.getCont());
		
		this.asiento = asiento;
		this.precio = vuelo.getPrecio();
		this.vuelo = vuelo;
		this.nombrePasajero = nombrePasajero;
		this.numDocPasajero = numDocPasajero;
		this.tipoDocPasajero = tipoDocPasajero;
		this.isActive=true;
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
	public void setNombrePasajero(String nombrePasajero) throws EValorNulo, EInvalidName {
		validarNombre(nombrePasajero);
		this.nombrePasajero = nombrePasajero;	
	}
	public String getNumDocPasajero() {return numDocPasajero;}
	public void setNumDocPasajero(String numDocPasajero) throws EValorNulo, EInvalidDocumento {
		validarDocumento(numDocPasajero);
		this.numDocPasajero = numDocPasajero;	
	}
	public String getTipoDocPasajero() {return tipoDocPasajero;}
	public void setTipoDocPasajero(String tipoDocPasajero) throws EValorNulo, EInvalidDocumento {
		validarTipoDoc(tipoDocPasajero);
		this.tipoDocPasajero = tipoDocPasajero;	
	}
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	private void validarTipoDoc(String tipoDocumento) throws EValorNulo, EInvalidDocumento {

        Valida.validarTexto(tipoDocumento, "El tipo de documento no puede ser null ni vacío");
        if (!tipoDocumento.equalsIgnoreCase("CC") &&
            !tipoDocumento.equalsIgnoreCase("CE") &&
            !tipoDocumento.equalsIgnoreCase("PAS")) {

            throw new EInvalidDocumento("El tipo de documento debe ser: CC, CE o PAS");
        }
    }
	private void validarDocumento(String documento) throws EValorNulo, EInvalidDocumento {
        Valida.validarTexto(documento, "El documento no puede ser null ni vacío");
        if(!documento.matches("[0-9]+"))
            throw new EInvalidDocumento("El documento solo puede contener números");
        if (documento.length()<7 || documento.length()>10)
            throw new EInvalidDocumento("El documento debe tener entre 7 y 10 dígitos");
    }
	private void validarNombre(String nombre) throws EValorNulo, EInvalidName {
        Valida.validarTexto(nombre, "El nombre no puede ser null ni vacío");
        if (!nombre.matches("^[a-zA-ZáéíóúüñÑ\\s]+$"))//regex
            throw new EInvalidName("EL nombre solo puede contener letras");
    }
	
	public void wFicheroTiquete(String dir) throws IOException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Tiquete)this);
		b.close();
		f.close();
	}
	public static Tiquete rFicheroTiquete(String dir) throws IOException, ClassNotFoundException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Tiquete tiquete = (Tiquete) b.readObject();
		b.close();
		f.close();
		return tiquete;
	}

}
