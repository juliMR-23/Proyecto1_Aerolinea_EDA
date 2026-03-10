package principal;

import java.util.Arrays;
import java.io.IOException;
import java.io.Serializable;

import excepciones.EIDRepetido;
import excepciones.EInvalidDocumento;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNulo;
import util.Valida;

public class Cliente extends Persona implements Serializable {

	private Reserva[] reservas;
	private static final long serialVersionUID = 1L;

	public Cliente(String nombre, String tipoDocumento, String documento, String telefono, String email, String password) throws EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {
		super(nombre, tipoDocumento, documento, telefono, email, password);
		this.reservas = new Reserva[0];
	}

	public void addReserva(Vuelo vuelo) throws EValorNulo{
		if (vuelo == null)
			throw new EValorNulo("El vuelo no puede ser nulo");
		
		Reserva r = new Reserva(vuelo, this);
		reservas = Arrays.copyOf(reservas, reservas.length + 1);
		reservas[reservas.length -1] = r;
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

	    if (i != -1) {
	        for (int j= i; j < reservas.length-1; j++)
	            reservas[j] = reservas[j+1];

	        reservas = Arrays.copyOf(reservas, reservas.length -1);
	    }
	}

	public void addTiqueteOnReserva(Reserva reserva, String asiento, String nombrePasajero, String numDoc, String tipoDoc) throws EValorNulo {
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		int i = indexReserva(reserva.getId()); 
		if(i!=-1)
			reservas[i].addTiquete(asiento, nombrePasajero, numDoc, tipoDoc);
	}

	public int indexTiqueteOnReserva(Reserva reserva, String id) throws EValorNulo{
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		return reserva.indexTiquete(id);
	}

	public Tiquete searchTiqueteOnReserva(Reserva reserva, String id) throws EValorNulo{
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		return reserva.searchTiquete(id);
	}

	public void deleteTiquete(Reserva reserva, String id) throws EValorNulo{
		if (reserva == null)
			throw new EValorNulo("La reserva no puede ser nula");
		reserva.deleteTiquete(id);
	}

	public Reserva[] getReservas() {
		return reservas;
	}
}
