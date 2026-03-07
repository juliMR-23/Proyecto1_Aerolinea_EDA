package principal;

import java.util.Arrays;
import java.io.IOException;
import excepciones.EClienteInvalido;
import excepciones.EPersonaInvalida;
import excepciones.noIdException;

public class Cliente extends Persona {

	private Reserva[] reservas;

	public Cliente(String id, String nombre, String tipoDocumento, String documento, String telefono, String email) throws EPersonaInvalida {
		super(id, nombre, tipoDocumento, documento, telefono, email);
		this.reservas = new Reserva[0];
	}

	public void addReserva(String id, Vuelo vuelo, String dirFich) throws EClienteInvalido, IOException {
		if (id == null || id.isEmpty())
			throw new EClienteInvalido("El id de la reserva no puede ser nulo ni vacío");
		if (vuelo == null)
			throw new EClienteInvalido("El vuelo no puede ser nulo");
		if (indexReserva(id) != -1)
			throw new EClienteInvalido("Ya existe una reserva con ese id");
		Reserva r = new Reserva(id, vuelo, this, dirFich);
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

	public Reserva searchReserva(String id) throws EClienteInvalido {
		int i = indexReserva(id);
		if (i == -1)
			throw new EClienteInvalido("No existe una reserva con el id: " + id);
		return reservas[i];
	}

	public void deleteReserva(String id) throws EClienteInvalido {
		int i = indexReserva(id);
		if (i == -1)
			throw new EClienteInvalido("No existe una reserva con el id: " + id);
		reservas[i] = reservas[reservas.length - 1];
		reservas = Arrays.copyOf(reservas, reservas.length - 1);
	}

	public void addTiqueteOnReserva(Reserva reserva, String id, String asiento, double precio, String nombrePasajero, String numDoc, String tipoDoc, String dirFich) throws EClienteInvalido, IOException {
		if (reserva == null)
			throw new EClienteInvalido("La reserva no puede ser nula");
		reserva.addTiquete(id, asiento, precio, nombrePasajero, numDoc, tipoDoc, dirFich);
	}

	public int indexTiqueteOnReserva(Reserva reserva, String id) throws EClienteInvalido, noIdException {
		if (reserva == null)
			throw new EClienteInvalido("La reserva no puede ser nula");
		return reserva.indexTiquete(id);
	}

	public Tiquete searchTiqueteOnReserva(Reserva reserva, String id) throws EClienteInvalido, noIdException {
		if (reserva == null)
			throw new EClienteInvalido("La reserva no puede ser nula");
		return reserva.searchTiquete(id);
	}

	public void deleteTiquete(Reserva reserva, String id) throws EClienteInvalido, noIdException {
		if (reserva == null)
			throw new EClienteInvalido("La reserva no puede ser nula");
		reserva.deleteTiquete(id);
	}

	public Reserva[] getReservas() {
		return reservas;
	}
}