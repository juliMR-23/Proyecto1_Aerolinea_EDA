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

	public String[] cargarAviones() throws EValorNulo{ 
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
	        for (int j = i; j < aviones.length - 1; j++)
	            aviones[j] = aviones[j + 1];//evita espacios vacíos, mismo orden
	        aviones = Arrays.copyOf(aviones, aviones.length - 1);//reducir arreglo (elimina última pos)
	    }
	}
	public Avion[] listAviones() {
		return aviones;
	}
	public Avion[] listAvionesActivos() {
		Avion[] activos = new Avion[0];
		for(Avion a: aviones) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
	}
	
	
	//lo mismo para las otras listas
	public void addAeropuerto(String nombre, String ciudad, String pais, String zonaHoraria, double longitud, double latitud) throws EIDRepetido, EValorNulo {

		Aeropuerto a = new Aeropuerto(nombre, ciudad, pais, zonaHoraria, longitud, latitud);
		aeropuertos = Arrays.copyOf(aeropuertos, aeropuertos.length + 1);
        aeropuertos[aeropuertos.length - 1] = a;
    }

    
    public void guardarAeropuerto() throws IOException, EValorNulo {
    	for(int i=0; i < aeropuertos.length; i++) {
    		Aeropuerto a = aeropuertos[i];
    		a.wFicheroAeropuerto("src/ficheros/aeropuertos/Aeropuerto"+(i+1)+".ae");;
    	}
    }
    
    public String[] cargarAeropuerto() { 
    	File dir = new File("src/ficheros/aeropuertos/"); 
    	String[] errores = new String[0];
    	
    	if(dir.exists()) {
    		File[] ficheros = dir.listFiles();
    		if(ficheros != null) {
    			for(File f: ficheros) {
    				if(f.isFile() && f.getName().endsWith(".ae")) { 
    					try {
    						Aeropuerto a = Aeropuerto.rFicheroAeropuerto(f.getPath());
    						aeropuertos = Arrays.copyOf(aeropuertos, aeropuertos.length+1);
    						aeropuertos[aeropuertos.length-1] = a;
    						
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
    
    public Aeropuerto[] listAeropuertosActivos() {
    	Aeropuerto[] activos = new Aeropuerto[0];
		for(Aeropuerto a: aeropuertos) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
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
            c.wFicheroPersona("src/ficheros/clientes/cliente"+(i+1)+".cl");
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
                        Cliente cliente = Cliente.rFicheroPersona(f.getPath());
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

    public Cliente[] listClientesActivos() {
    	Cliente[] activos = new Cliente[0];
		for(Cliente a: clientes) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
	}
    
    //Piloto y Tripulante

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
    
    public void guardarTripulantesCabina() throws IOException, EValorNulo {
    	for(int i=0; i < empleados.length; i++) {
    		TripulanteCabina tc = (TripulanteCabina) empleados[i];
    		tc.wFicheroPersona("src/ficheros/empleados/Tripulante"+(i+1)+".tc");;
    	}
    }
    
    public void guardarPilotos() throws IOException, EValorNulo {
    	for(int i=0; i < empleados.length; i++) {
    		Piloto a = (Piloto) empleados[i];
    		a.wFicheroPersona("src/ficheros/empleados/Piloto"+(i+1)+".pi");;
    	}
    }
    
    
    public String[] cargarEmpleados() { //Cambiar nombre 
    	File dir = new File("src/ficheros/empleado/"); //cambiar direccion 
    	String[] errores = new String[0];
    	
    	if(dir.exists()) {
    		File[] ficheros = dir.listFiles();
    		if(ficheros != null) {
    			for(File f: ficheros) {
    				if(f.isFile() && f.getName().endsWith(".tc")) { //Cambiar extension
    					try {
    						TripulanteCabina tc = TripulanteCabina.rFicheroPersona(f.getPath());
    						empleados = Arrays.copyOf(empleados, empleados.length+1);
    						empleados[empleados.length-1] = tc;
    						
    					} catch (IOException | ClassNotFoundException e) {
    						errores = Arrays.copyOf(errores, errores.length+1);
    						errores[errores.length-1] = f.getName() + ": " + e.getMessage();
     					}
    			}
    				
    				if(f.isFile() && f.getName().endsWith(".pi")) {
    					try {
    						Piloto p = Piloto.rFicheroPersona(f.getPath());
    						empleados = Arrays.copyOf(empleados, empleados.length+1);
    						empleados[empleados.length-1] = p;
    						
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
    
    public Empleado[] listEmpleadosActivos() {
    	Empleado[] activos = new Empleado[0];
		for(Empleado a: empleados) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
	}
    
    // Vuelos

    public void addVuelo(Aeropuerto origen, Aeropuerto destino, LocalDateTime fechaHoraSalida, Avion avion,TripulanteCabina[] tripulacion, Piloto[] pilotos, double precio) throws EIDRepetido, EValorNulo, EPilotosInsuficientes, EValorNegativo {


        Vuelo v = new Vuelo(origen, destino, fechaHoraSalida, avion, tripulacion, pilotos);
        vuelos = Arrays.copyOf(vuelos, vuelos.length + 1);
        vuelos[vuelos.length - 1] = v;
    }
    
    public void guardarVuelo() throws IOException, EValorNulo {
    	for(int i=0; i < vuelos.length; i++) {
    		Vuelo v = vuelos[i];
    		v.wFicheroVuelo("src/ficheros/vuelos/Vuelo"+(i+1)+".vu");;
    	}
    }
    
    public String[] cargarVuelos() { //Cambiar nombre 
    	File dir = new File("src/ficheros/vuelos"); //cambiar direccion 
    	String[] errores = new String[0];
    	
    	if(dir.exists()) {
    		File[] ficheros = dir.listFiles();
    		if(ficheros != null) {
    			for(File f: ficheros) {
    				if(f.isFile() && f.getName().endsWith(".vu")) { //Cambiar extension
    					try {
    						Vuelo v = Vuelo.rFicheroVuelo(f.getPath());
    						vuelos = Arrays.copyOf(vuelos, vuelos.length+1);
    						vuelos[vuelos.length-1] = v;
    						
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
    
    public Vuelo[] listVuelosActivos() {
    	Vuelo[] activos = new Vuelo[0];
		for(Vuelo a: vuelos) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
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
            a.wFicheroPersona("src/ficheros/administradores/administrador"+(i+1)+".adm");
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
                        Administrador administrador = Administrador.rFicheroPersona(f.getPath());
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
    
    public Administrador[] listAdministradoresActivos() {
    	Administrador[] activos = new Administrador[0];
		for(Administrador a: administradores) {
			if (a.isActive()) {
				activos=Arrays.copyOf(activos,activos.length+1);
				activos[activos.length-1]=a;
			}
		}
		return activos;
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

	public String[] cargarTodo() {
		// TODO Auto-generated method stub
		return null;
	}

	public void guardarTodo() {
		// TODO Auto-generated method stub
		
	}
}
