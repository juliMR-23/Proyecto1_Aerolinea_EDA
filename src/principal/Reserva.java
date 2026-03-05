package principal;

import java.util.Arrays;

import Excepciones.noIdException;

public class Reserva {
	String id;
	Cliente cliente;
	Tiquete[] tiquetes = new Tiquete[0];
	int descuento;
	
	
	
	public Reserva(String id, Cliente cliente, int descuento) {
		this.id = id;
		this.cliente = cliente;
		this.descuento = descuento;
	}

	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	public Cliente getCliente() {return cliente;}
	public void setCliente(Cliente cliente) {this.cliente = cliente;}
	public int getDescuento() {return descuento;}
	public void setDescuento(int descuento) {this.descuento = descuento;}

	public Tiquete[] getTiquetes() {return tiquetes;}

	public void anadirTiquete(String id, String asiento, String clase, float precioInicial, Vuelo vuelo, String nombrePasajero,
			String numDocPasajero, String tipoDocPasajero) {
		tiquetes = Arrays.copyOf(tiquetes, tiquetes.length+1);
		tiquetes[tiquetes.length-1] = new Tiquete(id, asiento, clase, (precioInicial*((100-descuento)/100)), vuelo, nombrePasajero, numDocPasajero, tipoDocPasajero, true);
	}
	
	public Tiquete buscarTiquetePorPasajero(String tipoDoc, String numDoc) throws noIdException {
		for(Tiquete t: tiquetes) {
			if (t.getTipoDocPasajero().equals(tipoDoc) && t.getNumDocPasajero().equals(numDoc)) {
				return t;
			}
		}
		throw new noIdException("No existe un tiquete en esta reserva asignado a ese pasajero");
	}
	
	public void deleteTiquete(Tiquete tiquete) throws noIdException{
		int i = 0;
		while(tiquetes[i]!=tiquete && i!= tiquetes.length) {
			i++;
		}
		if (i==tiquetes.length) {
			throw new noIdException("Ese tiquete no existe");
		}
		
		for(int j = i; j<tiquetes.length-1; j++) {
			tiquetes[j]=tiquetes[j+1];
		}
		tiquetes=Arrays.copyOf(tiquetes, tiquetes.length-1);
	}
	
	public float calcularPrecioTotal() {
		float total = 0;
		for(Tiquete t:tiquetes) {
			total+=t.getPrecio();
		}
		return total;
	}
}