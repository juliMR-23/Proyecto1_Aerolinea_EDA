package principal;

import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import excepciones.EIDRepetido;
import excepciones.EInvalidDocumento;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EValorNulo;
import util.IDAsign;
import util.Valida;

public class Cliente extends Persona implements Serializable {

	private Reserva[] reservas;
	private static final long serialVersionUID = 1L;

	public Cliente(String nombre, String tipoDocumento, String documento, String telefono, String email, String password) throws EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {
		super(nombre, tipoDocumento, documento, telefono, email, password);
		this.reservas = new Reserva[0];
		this.id=IDAsign.asignar("CL", cont);
	}

	public void addReserva(Vuelo vuelo) throws EValorNulo{
		if (vuelo == null)
			throw new EValorNulo("El vuelo no puede ser nulo");
		
		Reserva r = new Reserva(vuelo, this);
		reservas = Arrays.copyOf(reservas, reservas.length + 1);
		reservas[reservas.length -1] = r;
	}
	
	public void guardarReservas() throws IOException, EValorNulo {
        for(int i = 0; i < reservas.length; i++) {
            Reserva r = reservas[i];
            r.wFicheroReserva("src/ficheros/reservas/reserva"+(i+1)+".res");
        }
    }

	public String[] cargarReservas() throws EValorNulo{ 
        File dir = new File("src/ficheros/reservas/");
        String[] errores = new String[0]; //Añadido

        if (dir.exists()) {
            File[] ficheros = dir.listFiles();
            if (ficheros != null) {
                for(File f: ficheros) {
                    if(f.isFile() && f.getName().endsWith(".res")) {

                    	try {
                        Reserva reserva = Reserva.rFicheroReserva(f.getPath());
                        reservas = Arrays.copyOf(reservas, reservas.length + 1);
                        reservas[reservas.length - 1] = reserva; 

                        } catch (IOException | ClassNotFoundException e) {
                        	errores = Arrays.copyOf(errores, errores.length+1);
                        	errores[errores.length-1] = f.getName() + ": " + e.getMessage();
                        }
                    }
                }
            }
        }
        return errores;
    }

	public int indexReserva(String id) {
		int n = 0;
		while (n < reservas.length && !reservas[n].getId().equalsIgnoreCase(id)) {
			n++;
		}
		if (n < reservas.length)
			if(reservas[n].isActive()) 
				return n;
		return -1;
	}
	
	public Reserva[] listReservas() {
		return reservas;
	}

	public Reserva[] listReservasActivas() {
		Reserva[] activos = new Reserva[0];
		for(Reserva a: reservas) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
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
	
	@Override
	public void wFicheroPersona(String dir) throws IOException {

		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Cliente)this);
		b.close();
		f.close();
	}

	public static Cliente rFicheroPersona(String dir) throws IOException, ClassNotFoundException {

		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Cliente cliente = (Cliente) b.readObject();
		b.close();
		f.close();
		return cliente;
	}
}
