package principal;

import java.util.Date;

abstract class Empleado extends Persona {
	
	// ATRIBUTOS
	private double salarioBase;
	private Date fechaContratacion;
	private boolean activo;
	private int aniosExperiencia;
	private Vuelo[] vuelosAsignados;
	private Aerolinea aerolinea;
	private int cantidadVuelos;
	private static final int MAX_VUELOS = 10;
	
	// CONSTRUCTOR
	public Empleado (String id, String nombre, String tipoDocumento, String documento, String telefono, String email, double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia, Aerolinea aerolinea) {
		super(id, nombre, tipoDocumento, documento, telefono, email);
		this.salarioBase = salarioBase;
		this.fechaContratacion = fechaContratacion;
		this.activo = activo;
		this.aniosExperiencia = aniosExperiencia;
		this.aerolinea = aerolinea;
		this.vuelosAsignados = new Vuelo[MAX_VUELOS];
    	this.cantidadVuelos = 0;
	}
		
	// METODOS
	public abstract double calcularSalario();
	
	// Metodo para asignar vuelos al Empleado
	public void asignarVuelo(Vuelo vuelo) {

    	if (this.cantidadVuelos >= this.vuelosAsignados.length) {

        	System.out.println("No hay espacio para más vuelos.");
        	return;
    	}

    	// verificar que no exista ya ese id
    	for (int i = 0; i < this.cantidadVuelos; i++) {

        	if (this.vuelosAsignados[i].getId() == vuelo.getId()) {

            	System.out.println("El vuelo ya está asignado.");
            	return;
        	}
    	}

    	this.vuelosAsignados[this.cantidadVuelos] = vuelo;
    	this.cantidadVuelos++;
	}

	// Metodo para buscar entre los vuelos asignados al empleado
	public Vuelo[] buscarVuelosAsignados(Vuelo vuelo) {

    	int contador = 0;

	    for (int i = 0; i < this.cantidadVuelos; i++) {
    	    if (this.vuelosAsignados[i].getId() == vuelo.getId()) {
            	contador++;
        	}
    	}

    	Vuelo[] encontrados = new Vuelo[contador];
    	int index = 0;

    	for (int i = 0; i < this.cantidadVuelos; i++) {
        	if (this.vuelosAsignados[i].getId() == vuelo.getId()) {
	            encontrados[index] = this.vuelosAsignados[i];
    	        index++;
	        }
    	}

    	return encontrados;
	}

	// Metodo para eliminar un vuelo asignado a un empleado
	public void eliminarVueloAsignado(Vuelo vuelo) {

    	for (int i = 0; i < this.cantidadVuelos; i++) {

        	if (this.vuelosAsignados[i].getId() == vuelo.getId()) {

            	// mover elementos hacia la izquierda
            	for (int j = i; j < cantidadVuelos - 1; j++) {
                	this.vuelosAsignados[j] = this.vuelosAsignados[j + 1];
            	}

            	this.vuelosAsignados[cantidadVuelos - 1] = null;
            	this.cantidadVuelos--;

            	System.out.println("Vuelo eliminado.");
            	return;
        	}
    	}

    	System.out.println("Vuelo no encontrado.");
	}
		
	// GETTERS
	public double getSalarioBase() {
		return this.salarioBase;
	}
	
	public Date getFechaContratacion() {
		return this.fechaContratacion;
	}
	
	public boolean isActivo() {
		return this.activo;
	}
	
	public int getAniosExperiencia() {
		return this.aniosExperiencia;
	}

	public Aerolinea getAerolinea() {
		return this.aerolinea;
	}
			
	// SETTERS
	public void setSalarioBase(double salarioBase) {
		this.salarioBase = salarioBase;
	}
	
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	public void setAniosExperiencia(int aniosExperiencia) {
		this.aniosExperiencia = aniosExperiencia;
	}

	public void setAerolinea(Aerolinea aerolinea) {
		this.aerolinea = aerolinea;
	}
	
	
}