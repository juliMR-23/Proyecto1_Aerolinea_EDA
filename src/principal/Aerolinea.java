package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import excepciones.EIDRepetido;
import excepciones.EValorNegativo;
import excepciones.EValorNulo;

public class Aerolinea implements Serializable{
	private String nombre;
	private Aeropuerto[] aeropuertos;
	private Avion[] aviones;
	private Cliente[] clientes;
	private Empleado[] empleados;
	private Vuelo[] vuelos;
	
	public Aerolinea(String nombre) throws EValorNulo {
	    if (nombre== null || nombre.trim().isEmpty())
	        throw new EValorNulo("El nombre no puede estar vacío");
		this.nombre = nombre;
        this.aeropuertos = new Aeropuerto[0];
        this.aviones = new Avion[0];
        this.clientes = new Cliente[0];
        this.empleados = new Empleado[0];
        this.vuelos = new Vuelo[0];
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) throws EValorNulo {
		if (nombre == null || nombre.trim().isEmpty())
	        throw new EValorNulo("El nombre no puede estar vacío");
		this.nombre = nombre;
	}
	
	public void addAvion(String matricula, String marca, String modelo, int capacidad) throws EIDRepetido, EValorNulo, EValorNegativo {
		if(indexAvion(matricula)!=-1)
			throw new EIDRepetido("Ya existe otro avion con este id");//implementar en las demás listas cuando creen todas las clases
		
		Avion a = new Avion(matricula, marca, modelo, capacidad, true);
		aviones = Arrays.copyOf(aviones, aviones.length+1);
		aviones[aviones.length-1]=a;
	}
	public Avion searchAvion(String id) {
		int i=indexAvion(id);
		if(i==-1)
			return null;
		return aviones[i];
	}
	public int indexAvion(String id) {
		int n=0;
		while(n<aviones.length && !aviones[n].getMatricula().equalsIgnoreCase(id.replace(" ", ""))) {
			n++;
		}
		if(n<aviones.length)
			return n;
		return -1;//si no lo encuentra retorna -1
	}
	public void deleteAvion(String id) {
		int i = indexAvion(id);
		if(i!=-1) {//el avion existe, está en la lista
			aviones[i]=aviones[aviones.length-1];//evita espacios vacíos
			aviones = Arrays.copyOf(aviones, aviones.length-1);//reducir arreglo (elimina última pos)
		}
	}
	public Avion[] listAviones() {
		return aviones;
	}
	
	/*
	//ESPERAR A QUE LOS DEMÁS CREEN LAS CLASES
	public void addAeropuerto(Aeropuerto aeropuerto) {
        aeropuertos = Arrays.copyOf(aeropuertos, aeropuertos.length + 1);
        aeropuertos[aeropuertos.length - 1] = aeropuerto;
    }

    public Aeropuerto searchAeropuerto(String id) {
        int i = indexAeropuerto(id);
        if(i==-1)
            return null;
        return aeropuertos[i];
    }

    public int indexAeropuerto(String id) {
        int n = 0;
        while(n < aeropuertos.length && !aeropuertos[n].getId().equalsIgnoreCase(id)) {
            n++;
        }
        if(n < aeropuertos.length)
            return n;
        return -1;
    }

    public void deleteAeropuerto(String id) {
        int i = indexAeropuerto(id);
        if(i != -1) {
            aeropuertos[i] = aeropuertos[aeropuertos.length - 1];
            aeropuertos = Arrays.copyOf(aeropuertos, aeropuertos.length - 1);
        }
    }

    public Aeropuerto[] listAeropuertos() {
        return aeropuertos;
    }

    public void addCliente(Cliente cliente) {
        clientes = Arrays.copyOf(clientes, clientes.length + 1);
        clientes[clientes.length - 1] = cliente;
    }

    public Cliente searchCliente(String id) {
        int i = indexCliente(id);
        if(i == -1)
            return null;
        return clientes[i];
    }

    public int indexCliente(String id) {
        int n = 0;
        while(n < clientes.length && !clientes[n].getId().equalsIgnoreCase(id)) {
            n++;
        }
        if(n < clientes.length)
            return n;
        return -1;
    }

    public void deleteCliente(String id) {
        int i = indexCliente(id);
        if(i != -1) {
            clientes[i] = clientes[clientes.length - 1];
            clientes = Arrays.copyOf(clientes, clientes.length - 1);
        }
    }

    public Cliente[] listClientes() {
        return clientes;
    }

    public void addEmpleado(Empleado empleado) {
        empleados = Arrays.copyOf(empleados, empleados.length + 1);
        empleados[empleados.length - 1] = empleado;
    }

    public Empleado searchEmpleado(String id) {
        int i = indexEmpleado(id);
        if(i == -1)
            return null;
        return empleados[i];
    }

    public int indexEmpleado(String id) {
        int n = 0;
        while(n < empleados.length && !empleados[n].getId().equalsIgnoreCase(id)) {
            n++;
        }
        if(n < empleados.length)
            return n;
        return -1;
    }

    public void deleteEmpleado(String id) {
        int i = indexEmpleado(id);
        if(i != -1) {
            empleados[i] = empleados[empleados.length - 1];
            empleados = Arrays.copyOf(empleados, empleados.length - 1);
        }
    }

    public Empleado[] listEmpleados() {
        return empleados;
    }

    public void addVuelo(Vuelo vuelo) {
        vuelos = Arrays.copyOf(vuelos, vuelos.length + 1);
        vuelos[vuelos.length - 1] = vuelo;
    }

    public Vuelo searchVuelo(String id) {
        int i = indexVuelo(id);
        if(i == -1)
            return null;
        return vuelos[i];
    }

    public int indexVuelo(String id) {
        int n = 0;
        while(n < vuelos.length && !vuelos[n].getId().equalsIgnoreCase(id)) {
            n++;
        }
        if(n < vuelos.length)
            return n;
        return -1;
    }

    public void deleteVuelo(String id) {
        int i = indexVuelo(id);
        if(i != -1) {
            vuelos[i] = vuelos[vuelos.length - 1];
            vuelos = Arrays.copyOf(vuelos, vuelos.length - 1);
        }
    }

    public Vuelo[] listVuelos() {
        return vuelos;
    }    
    */

	public void wFicheroAerolinea(String dir) throws IOException {
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Aerolinea)this);
		b.close();
		f.close();
	}
	public static Aerolinea rFicheroAerolinea(String dir) throws IOException, ClassNotFoundException {
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Aerolinea a = (Aerolinea) b.readObject();
	    f.close();
	    b.close();
	    return a;
	}
	
}
