package principal;

import java.io.File;
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
import excepciones.EInvalidDocumento;
import excepciones.EPilotosInsuficientes;
import excepciones.EValorNegativo;
import excepciones.EValorNulo;
import util.IDAsign;
import util.Valida;

public class Aerolinea implements Serializable{
	private String nombre;
	private Aeropuerto[] aeropuertos;
	private Avion[] aviones;
	private Cliente[] clientes;
	private Empleado[] empleados;
	private Vuelo[] vuelos;
	private Administrador[] administradores;
	private static int cont=0;
	private static final long serialVersionUID = 1L;
	
	
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
	
	public void guardarAviones() throws IOException {
        for(int i = 0; i < aviones.length; i++) {
            Avion a = aviones[i];
            a.wFicheroAvion("src/ficheros/aviones/avion"+(i+1)+".av");
        }
    }

	public String[] cargarAviones(){ 
        File dir = new File("src/ficheros/aviones/");
        String[] errores = new String[0]; //Añadido
        
        if (dir.exists()) {
            File[] ficheros = dir.listFiles();
            if (ficheros != null) {
                for(File f: ficheros) {
                    if(f.isFile() && f.getName().endsWith(".av")) {
                    	
                    	try {
                        Avion avion = Avion.rFicheroAvion(f.getPath());
                        aviones = Arrays.copyOf(aviones, aviones.length + 1);
                        aviones[aviones.length - 1] = avion; 
                        
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
			if(aviones[n].isActive()) 
				return n;
		return -1;//si no lo encuentra retorna -1
	}
	public void deleteAvion(String id) {
	    int i = indexAvion(id);
	    if (i != -1) {//el avion existe, está en la lista
	    	aviones[i].setActive(false);
	    }
	}
	public Avion[] listAviones() {
		return aviones;
	}
	
	
	//lo mismo para las otras listas
	public void addAeropuerto(String nombre, String ciudad, String pais, String codigoIATA, String zonaHoraria, double longitud, double latitud) throws EIDRepetido, EValorNulo {

		Aeropuerto a = new Aeropuerto(nombre, ciudad, pais, zonaHoraria, longitud, latitud);
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
        	if(aeropuertos[n].isActive()) 
				return n;
        return -1;
    }

    public void deleteAeropuerto(String id) {
        int i = indexAeropuerto(id);
        if (i != -1) {
            aeropuertos[i].setActive(false);
        }
    }

    public Aeropuerto[] listAeropuertos() {
        return aeropuertos;
    }

    public void addCliente(String nombre, String tipoDocumento, String documento, String telefono, String email, String password) 
            throws EIDRepetido, EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {

        if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        
        Cliente c = new Cliente(nombre, tipoDocumento, documento, telefono, email, password);
        if(indexCliente(c.getId()) != -1)
            throw new EIDRepetido("Ya existe otro cliente con este id");
        clientes = Arrays.copyOf(clientes, clientes.length + 1);
        clientes[clientes.length - 1] = c;
    }
    
	
	public void guardarClientes() throws IOException {
        for(int i = 0; i < clientes.length; i++) {
            Cliente c = clientes[i];
            c.copiarFicheroPersona("src/ficheros/clientes/cliente"+(i+1)+".cl");
        }
    }

	public String[] cargarClientes(){ 
        File dir = new File("src/ficheros/clientes/");
        String[] errores = new String[0]; //Añadido
        
        if (dir.exists()) {
            File[] ficheros = dir.listFiles();
            if (ficheros != null) {
                for(File f: ficheros) {
                    if(f.isFile() && f.getName().endsWith(".cl")) {
                    	
                    	try {
                        Cliente cliente = Cliente.leerFicheroPersona(f.getPath());
                        clientes = Arrays.copyOf(clientes, clientes.length + 1);
                        clientes[clientes.length - 1] = cliente; 
                        
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
        	if(clientes[n].isActive()) 
				return n;
        return -1;
    }
    public void deleteCliente(String id) {
        int i = indexCliente(id);
        if (i != -1) {
        	clientes[i].setActive(false);
        }
    }
    public Cliente[] listClientes() {
        return clientes;
    }

    public void addPiloto(String nombre, String tipoDocumento, String documento, String telefono, String email, String password,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia) throws EIDRepetido, EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {
        
        if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        Piloto p = new Piloto(nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, activo, aniosExperiencia);
        if(indexEmpleado(p.getId()) != -1)
            throw new EIDRepetido("Ya existe otro empleado con este id");
        empleados = Arrays.copyOf(empleados, empleados.length + 1);
        empleados[empleados.length - 1] = p;
    }
    public void addTripulanteCabina(String nombre, String tipoDocumento, String documento, String telefono, String email, String password,
			double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia)  throws EIDRepetido, EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento{
    	
    	if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        TripulanteCabina t = new TripulanteCabina(nombre, tipoDocumento, documento, telefono, email, password, salarioBase, fechaContratacion, activo, aniosExperiencia);
        if(indexEmpleado(t.getId()) != -1)
            throw new EIDRepetido("Ya existe otro empleado con este id");
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
        	if(empleados[n].isActive()) 
				return n;
        return -1;
    }

    public void deleteEmpleado(String id) {
        int i = indexEmpleado(id);
        if (i != -1) {
        	empleados[i].setActive(false);
        }
    }

    public Empleado[] listEmpleados() {
        return empleados;
    }

    public void addVuelo(Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaHoraSalida, Avion avion,TripulanteCabina[] tripulacion, Piloto[] pilotos, double precio) throws EIDRepetido, EValorNulo, EPilotosInsuficientes, EValorNegativo {
 

        Vuelo v = new Vuelo(origen, destino, fechaHoraSalida, avion, tripulacion, pilotos, precio);
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
        	if(vuelos[n].isActive()) 
				return n;
        return -1;
    }

    public void deleteVuelo(String id) {
        int i = indexVuelo(id);
        if (i != -1) {
        	vuelos[i].setActive(false);
        }
    }

    public Vuelo[] listVuelos() {
        return vuelos;
    }
    

    public void addAdministrador(String nombre, String tipoDocumento, String documento,
            String telefono, String email, String password)
            throws EIDRepetido, EValorNulo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento {

        if(existeEmail(email))
            throw new EInvalidEmail("Ya existe otra persona con este email");

        Administrador a = new Administrador(nombre, tipoDocumento, documento, telefono, email, password);
        if(indexAdministrador(a.getId()) != -1)
            throw new EIDRepetido("Ya existe otro administrador con este id");
        administradores = Arrays.copyOf(administradores, administradores.length + 1);
        administradores[administradores.length - 1] = a;
    }
	
	public void guardarAdministradores() throws IOException {
        for(int i = 0; i < administradores.length; i++) {
            Administrador a = administradores[i];
            a.copiarFicheroPersona("src/ficheros/administradores/administrador"+(i+1)+".adm");
        }
    }

	public String[] cargarAdministradores(){ 
        File dir = new File("src/ficheros/administradores/");
        String[] errores = new String[0]; //Añadido
        
        if (dir.exists()) {
            File[] ficheros = dir.listFiles();
            if (ficheros != null) {
                for(File f: ficheros) {
                    if(f.isFile() && f.getName().endsWith(".adm")) {
                    	
                    	try {
                        Administrador administrador = Administrador.leerFicheroPersona(f.getPath());
                        administradores = Arrays.copyOf(administradores, administradores.length + 1);
                        administradores[administradores.length - 1] = administrador; 
                        
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
        	if(administradores[n].isActive()) 
				return n;

        return -1;
    }

    public void deleteAdministrador(String id) {
        int i = indexAdministrador(id);

        if (i != -1) {
        	administradores[i].setActive(false);
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
        
        i = 0;
        while (i < administradores.length && !administradores[i].getEmail().equalsIgnoreCase(email)) i++;
        if(i<administradores.length)
        	return true;
        
        return false;
    }
    
    

	public static int getCont() {
		return cont;
	}
	public static void setCont(int cont) {
		Aerolinea.cont = cont;
	}
	public static void aumentaCont() {
		Aerolinea.cont++;
	}

	public void wFicheroAerolinea(String dir) throws IOException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
		FileOutputStream f = new FileOutputStream(dir);
		ObjectOutputStream b = new ObjectOutputStream(f);
		b.writeObject((Aerolinea)this);
		b.close();
		f.close();
	}
	public static Aerolinea rFicheroAerolinea(String dir) throws IOException, ClassNotFoundException, EValorNulo {
		Valida.validarTexto(dir, "La dirección del fichero no puede estar vacía");
	    FileInputStream f = new FileInputStream(dir);
	    ObjectInputStream b = new ObjectInputStream(f);
	    Aerolinea a = (Aerolinea) b.readObject();
	    b.close();
		f.close();
	    return a;
	}
}
