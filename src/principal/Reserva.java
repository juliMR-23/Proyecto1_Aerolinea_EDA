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
	private boolean isYet;
	private boolean isActive;
	
	private static final long serialVersionUID = 1L;
	
	public Reserva(Vuelo vuelo, Cliente cliente) throws EValorNulo {
		
		if(vuelo == null) {throw new EValorNulo("El vuelo no puede estar vacío");}
		if(cliente == null) {throw new EValorNulo("El cliente no puede estar vacío");}
		
		this.id = IDAsign.asignar("RE",Aerolinea.getCont());
		this.cliente = cliente;
		this.vuelo = vuelo;
		this.isYet = true;
		this.isActive = true;
		Aerolinea.aumentaCont();
	}
	public void validarYet() {
		if(vuelo.getFechaHoraSalida().isBefore(LocalDateTime.now())) {
			setYet(false);
		};
	}

	public String getId() {return id;}
	public void setId(String id) throws EValorNulo {
	    Valida.validarTexto(id, "El id no puede estar vacío");
	    this.id = id;
	}
	public Cliente getCliente() {return cliente;}
	public void setCliente(Cliente cliente) throws EValorNulo {
	    if(cliente == null)
	        throw new EValorNulo("El cliente no puede estar vacío");
	    this.cliente = cliente;
	}
	public Vuelo getVuelo() {return vuelo;}
	public void setVuelo(Vuelo vuelo) throws EValorNulo {
	    if(vuelo == null)
	        throw new EValorNulo("El vuelo no puede estar vacío");
	    this.vuelo = vuelo;
	}
	public boolean isActive() {return isActive;}
	public void setActive(boolean activa) {this.isActive = activa;}
	public boolean isyet() {return isYet;}
	public void setYet(boolean activa) {this.isYet = activa;}
	

	public void addTiquete(String asiento, String nombrePasajero, String numDocPasajero, String tipoDocPasajero) throws EValorNulo{
		Tiquete t=new Tiquete(asiento, this.vuelo, nombrePasajero, numDocPasajero, tipoDocPasajero);
		
		tiquetes = Arrays.copyOf(tiquetes, tiquetes.length+1);
		tiquetes[tiquetes.length-1] = t;
	}
	
	public void guardarTiquetes() throws IOException {
        for(int i = 0; i < tiquetes.length; i++) {
            Tiquete t = tiquetes[i];
            t.copiarFicheroTiquete("src/ficheros/tiquetes/tiquete"+(i+1)+".tiq");
        }
    }

	public String[] cargarTiquetes(){ 
        File dir = new File("src/ficheros/tiquetes/");
        String[] errores = new String[0]; //Añadido
        
        if (dir.exists()) {
            File[] ficheros = dir.listFiles();
            if (ficheros != null) {
                for(File f: ficheros) {
                    if(f.isFile() && f.getName().endsWith(".tiq")) {
                    	
                    	try {
                        Tiquete tiquete = Tiquete.leerFicheroTiquete(f.getPath());
                        tiquetes = Arrays.copyOf(tiquetes, tiquetes.length + 1);
                        tiquetes[tiquetes.length - 1] = tiquete; 
                        
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
	
	public int indexTiquete(String id) {
		int i = 0;
		while (i<tiquetes.length && !tiquetes[i].getId().equalsIgnoreCase(id)) {
			i++;
		}
		if (i < tiquetes.length)
			if(tiquetes[i].isActive()) 
				return i;
		return -1;
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
            	tiquetes[j] = tiquetes[j+1];
            tiquetes = Arrays.copyOf(tiquetes, tiquetes.length - 1);
        }
	}
	
	
	
	public Tiquete[] listTiquetes() {return tiquetes;}
	
	public Tiquete[] listTiquetesActivos() {
		Tiquete[] activos = new Tiquete[0];
		for(Tiquete a: tiquetes) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
	}
	
	public double calcularPrecioTotal() {
		double total = 0;
		for(Tiquete t:tiquetes) {
			total+=t.getPrecio();
		}
		return total;
	}
	
	public void copiarFicheroReserva(String dir) throws IOException {
		
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Reserva)this);
		b.close();
		f.close();
	}
	
	public static Reserva leerFicheroReserva(String dir) throws IOException, ClassNotFoundException {
		
		FileInputStream f = new FileInputStream(dir);
		ObjectInputStream b = new ObjectInputStream(f);
		Reserva reserva = (Reserva) b.readObject();
		b.close();
		f.close();
		return reserva;
	}
	
	
}
