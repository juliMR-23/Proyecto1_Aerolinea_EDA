package principal;

import java.util.Arrays;
import java.io.*;

import excepciones.EValorNegativo;
import excepciones.EValorNulo;
import excepciones.noIdException;
import util.IDAsign;
import util.Valida;

public class Reserva implements Serializable {
	private String id;
	private Vuelo vuelo;
	private Cliente cliente;
	private Tiquete[] tiquetes = new Tiquete[0];
	private boolean activa;
	private static int cont = 0;
	
	private static final long serialVersionUID = 1L;
	
	public Reserva(Vuelo vuelo, Cliente cliente) throws EValorNulo {
		
		if(vuelo == null) {throw new EValorNulo("El vuelo no puede estar vacío");}
		if(cliente == null) {throw new EValorNulo("El cliente no puede estar vacío");}
		
		this.id = IDAsign.asignar("RE",cont);
		this.cliente = cliente;
		this.vuelo = vuelo;
		this.activa = true;
		cont++;
	}

	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	public Cliente getCliente() {return cliente;}
	public void setCliente(Cliente cliente) {this.cliente = cliente;}
	public Vuelo getVuelo() {return vuelo;}
	public void setVuelo(Vuelo vuelo) {this.vuelo = vuelo;}
	public boolean isActiva() {return activa;}
	public void setActiva(boolean activa) {this.activa = activa;}
	public void setTiquetes(Tiquete[] tiquetes) {this.tiquetes = tiquetes;}
	public Tiquete[] getTiquetes() {return tiquetes;}

	public void addTiquete(String id, String asiento, String nombrePasajero, String numDocPasajero, String tipoDocPasajero) throws EValorNulo, EValorNegativo {
		tiquetes = Arrays.copyOf(tiquetes, tiquetes.length+1);
		tiquetes[tiquetes.length-1] = new Tiquete(id, asiento, this.vuelo, nombrePasajero, numDocPasajero, tipoDocPasajero);
	}
	
	public int indexTiquete(String id) throws noIdException {
		int i = 0;
		while (!tiquetes[i].equals(id)) {
			i++;
		}
		if (i==tiquetes.length) {throw new noIdException("No existe un tiquete en esta reserva asignado a ese pasajero");}
		return i;
	}
	
	public Tiquete searchTiquete(String id) throws noIdException{
		return (tiquetes[indexTiquete(id)]);
	}
	
	
	public void deleteTiquete(String id) throws noIdException{
		int i = 0;
		tiquetes[indexTiquete(id)]=tiquetes[tiquetes.length-1];
		tiquetes=Arrays.copyOf(tiquetes, tiquetes.length-1);
	}
	
	public double calcularPrecioTotal() {
		double total = 0;
		for(Tiquete t:tiquetes) {
			total+=t.getPrecio();
		}
		return total;
	}
	
	public void copiarFicheroReserva(String dir) throws IOException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Reserva)this);
		b.close();
		f.close();
	}
	public static Reserva leerFicheroReserva(String dir) throws IOException, ClassNotFoundException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Reserva reserva = (Reserva) b.readObject();
		b.close();
		f.close();
		return reserva;
	}
}
