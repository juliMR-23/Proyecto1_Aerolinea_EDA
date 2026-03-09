package principal;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import excepciones.EIDRepetido;
import excepciones.EInvalidEmail;
import excepciones.EInvalidPass;
import excepciones.EInvalidTelefono;
import excepciones.EPilotosInsuficientes;
import excepciones.EValorNegativo;
import excepciones.EValorNulo;
import util.Valida;

public class Aerolinea implements Serializable{
	private String nombre;
	private Aeropuerto[] aeropuertos;
	private Avion[] aviones;
	private Cliente[] clientes;
	private Empleado[] empleados;
	private Vuelo[] vuelos;
	private Administrador[] administradores;
	
	
	public Aerolinea(String nombre) throws EValorNulo {
	    Valida.validarTexto(nombre,"El nombre no puede estar vacío");
		this.nombre = nombre;
        this.aeropuertos = new Aeropuerto[0];
        this.aviones = new Avion[0];
        this.clientes = new Cliente[0];
        this.empleados = new Empleado[0];
        this.vuelos = new Vuelo[0];
        this.administradores = new Administrador[0];
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) throws EValorNulo {
		Valida.validarTexto(nombre,"El nombre no puede estar vacío");
		this.nombre = nombre;
	}
	
	public void addAvion(String matricula, String marca, String modelo, int capacidad, double velocidad) throws EIDRepetido, EValorNulo, EValorNegativo {
		if(indexAvion(matricula)!=-1)
			throw new EIDRepetido("Ya existe otro avion con este id");//implementar en las demás listas cuando creen todas las clases
		
		Avion a = new Avion(matricula, marca, modelo, capacidad, true, velocidad);
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
	
	
	//lo mismo para las otras listas
	public void addAeropuerto(String id, String nombre, String ciudad, String pais, String codigoIATA, String zonaHoraria, double longitud, double latitud) throws EIDRepetido, EValorNulo {
		if(indexAeropuerto(id)!=-1)
			throw new EIDRepetido("Ya existe otro aeropuerto con este id");
		
		Aeropuerto a = new Aeropuerto(nombre, ciudad, pais, codigoIATA, zonaHoraria, longitud, latitud);
		aeropuertos = Arrays.copyOf(aeropuertos, aeropuertos.length + 1);
        aeropuertos[aeropuertos.length - 1] = a;
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

    public void addCliente(String id, String nombre, String tipoDocumento, String documento, String telefono, String email, String password) 
            throws EIDRepetido, EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail {

        if(indexCliente(id) != -1)
            throw new EIDRepetido("Ya existe otro cliente con este id");
        if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        Cliente c = new Cliente(nombre, tipoDocumento, documento, telefono, email, password);

        clientes = Arrays.copyOf(clientes, clientes.length + 1);
        clientes[clientes.length - 1] = c;
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

    public void addPiloto(String id, String nombre, String tipoDocumento, String documento, String telefono, String email, String password,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia, Aerolinea aerolinea) throws EIDRepetido, EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail {
        if(indexEmpleado(id) != -1)
            throw new EIDRepetido("Ya existe otro empleado con este id");
        if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        Piloto p = new Piloto(nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, activo, aniosExperiencia);
        empleados = Arrays.copyOf(empleados, empleados.length + 1);
        empleados[empleados.length - 1] = p;
    }
    public void addTripulanteCabina(String id, String nombre, String tipoDocumento, String documento, String telefono, String email, String password,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia, Aerolinea aerolinea)  throws EIDRepetido, EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail{
    	if(indexEmpleado(id) != -1)
            throw new EIDRepetido("Ya existe otro empleado con este id");
    	if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        TripulanteCabina t = new TripulanteCabina(nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, activo, aniosExperiencia);
        empleados = Arrays.copyOf(empleados, empleados.length + 1);
        empleados[empleados.length - 1] = t;
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

    public void addVuelo(String id, Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaHoraSalida, Avion avion,TripulanteCabina[] tripulacion, Piloto[] pilotos) throws EIDRepetido, EValorNulo, EPilotosInsuficientes {
        if(indexVuelo(id) != -1)
            throw new EIDRepetido("Ya existe otro vuelo con este id");

        Vuelo v = new Vuelo(id, origen, destino, fechaHoraSalida, avion, tripulacion, pilotos);
        vuelos = Arrays.copyOf(vuelos, vuelos.length + 1);
        vuelos[vuelos.length - 1] = v;
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
    

    public void addAdministrador(String id, String nombre, String tipoDocumento, String documento,
            String telefono, String email, String password)
            throws EIDRepetido, EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail {

        if(indexAdministrador(id) != -1)
            throw new EIDRepetido("Ya existe otro administrador con este id");

        if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        Administrador a = new Administrador(nombre, tipoDocumento, documento, telefono, email, password);

        administradores = Arrays.copyOf(administradores, administradores.length + 1);
        administradores[administradores.length - 1] = a;
    }

    public Administrador searchAdministrador(String id) {
        int i = indexAdministrador(id);
        if(i == -1)
            return null;

        return administradores[i];
    }

    public int indexAdministrador(String id) {
        int n = 0;

        while(n < administradores.length && !administradores[n].getId().equalsIgnoreCase(id))
            n++;

        if(n < administradores.length)
            return n;

        return -1;
    }

    public void deleteAdministrador(String id) {
        int i = indexAdministrador(id);

        if(i != -1) {
            administradores[i] = administradores[administradores.length - 1];
            administradores = Arrays.copyOf(administradores, administradores.length - 1);
        }
    }

    public Administrador[] listAdministradores() {
        return administradores;
    }
    
    
    
    public boolean existeEmail(String email) {
        int i = 0;
        while (i < clientes.length && !clientes[i].getEmail().equalsIgnoreCase(email)) i++;
        if(i<clientes.length)
        	return true;

        i = 0;
        while (i < empleados.length && !empleados[i].getEmail().equalsIgnoreCase(email)) i++;
        if(i<empleados.length)
        	return true;
        
        return false;
    }
    

	public void wFicheroAerolinea(String dir) throws IOException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Aerolinea)this);
		f.close();
		b.close();
	}
	public static Aerolinea rFicheroAerolinea(String dir) throws IOException, ClassNotFoundException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Aerolinea a = (Aerolinea) b.readObject();
	    f.close();
	    b.close();
	    return a;
	}
}
