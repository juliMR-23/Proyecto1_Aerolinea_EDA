package principal;

import java.util.Arrays;
import java.io.IOException;
import java.io.Serializable;

import excepciones.EIDRepetido;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNulo;
import excepciones.noIdException;
import util.Valida;

public class Cliente extends Persona implements Serializable {

	private Reserva[] reservas;

	public Cliente(String id, String nombre, String tipoDocumento, String documento, String telefono, String email, String password) throws EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail {
		super(id, nombre, tipoDocumento, documento, telefono, email, password);
		this.reservas = new Reserva[0];
	}

	public void addReserva(String id, Vuelo vuelo) throws EValorNulo, EIDRepetido {
		if (vuelo == null)
			throw new EValorNulo("El vuelo no puede ser nulo");
		if (indexReserva(id) != -1)
			throw new EIDRepetido("Ya existe una reserva con ese id");
		Reserva r = new Reserva(id, vuelo, this);
		reservas = Arrays.copyOf(reservas, reservas.length + 1);
		reservas[reservas.length - 1] = r;
	}

	public int indexReserva(String id) {
		int n = 0;
		while (n < reservas.length && !reservas[n].getId().equalsIgnoreCase(id)) {
			n++;
		}
		if (n < reservas.length)
			return n;
		return -1;
	}

	public Reserva searchReserva(String id) {
		int i = indexReserva(id);
		if (i == -1)
			return null;
		return reservas[i];
	}

	public void deleteReserva(String id) {
		int i = indexReserva(id);
		if (i != -1){
			reservas[i] = reservas[reservas.length - 1];
			reservas = Arrays.copyOf(reservas, reservas.length - 1);
		}
	}

	public void addTiqueteOnReserva(Reserva reserva, String id, String asiento, double precio, String nombrePasajero, String numDoc, String tipoDoc, String dirFich) throws EValorNulo {
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		int i = indexReserva(reserva.getId()); 
		reservas[i].addTiquete(id, asiento, precio, nombrePasajero, numDoc, tipoDoc, dirFich);
	}

	public int indexTiqueteOnReserva(Reserva reserva, String id) throws EValorNulo, noIdException {
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		int i = indexReserva(reserva.getId()); 
		return reservas[i].indexTiquete(id);
	}

	public Tiquete searchTiqueteOnReserva(Reserva reserva, String id) throws EValorNulo, noIdException {
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		int i = indexReserva(reserva.getId()); 
		return reservas[i].searchTiquete(id);
	}

	public void deleteTiquete(Reserva reserva, String id) throws EValorNulo, noIdException {
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		int i = indexReserva(reserva.getId()); 
		reservas[i].deleteTiquete(id);
	}

	public Reserva[] getReservas() {
		return reservas;
	}
}
