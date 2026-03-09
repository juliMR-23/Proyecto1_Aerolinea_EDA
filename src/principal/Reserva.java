package principal;

import java.util.Arrays;
import java.io.*;
import java.time.LocalDateTime;

import excepciones.EValorNegativo;
import excepciones.EValorNulo;
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
	public void validarActive() {
		if(vuelo.getFechaHoraSalida().isBefore(LocalDateTime.now())) {
			setActiva(false);
		};
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

	public void addTiquete(String asiento, String nombrePasajero, String numDocPasajero, String tipoDocPasajero) throws EValorNulo{
		Tiquete t=new Tiquete(asiento, this.vuelo, nombrePasajero, numDocPasajero, tipoDocPasajero);
		tiquetes = Arrays.copyOf(tiquetes, tiquetes.length+1);
		tiquetes[tiquetes.length-1] = t;
	}
	
	public int indexTiquete(String id) {
		int i = 0;
		while (i<tiquetes.length && !tiquetes[i].getId().equalsIgnoreCase(id)) {
			i++;
		}
		if (i==tiquetes.length)
			return -1;
		return i;
	}
	
	public Tiquete searchTiquete(String id){
		int i = indexTiquete(id);
        if(i == -1)
            return null;
        return tiquetes[i];
	}
	
	
	public void deleteTiquete(String id){
		int i = indexTiquete(id);
        if (i != -1) {
            for (int j = i; j < tiquetes.length- 1; j++)
            	tiquetes[j] = tiquetes[j + 1];
            tiquetes = Arrays.copyOf(tiquetes, tiquetes.length - 1);
        }
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
		f.close();
		b.close();
	}
	public static Reserva leerFicheroReserva(String dir) throws IOException, ClassNotFoundException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Reserva reserva = (Reserva) b.readObject();
		f.close();
		b.close();
		return reserva;
	}
	public static int getCont() {
		return cont;
	}

	public static void setCont(int cont) {
		Reserva.cont = cont;
	}
}
