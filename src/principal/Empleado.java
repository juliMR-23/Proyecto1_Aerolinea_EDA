package principal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Date;


import excepciones.*;
import util.IDAsign;

public abstract class Empleado extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    // ATRIBUTOS
    protected double salarioBase;
    protected Date fechaContratacion;
    protected boolean activo;
    protected int aniosExperiencia;
    protected Vuelo[] vuelosAsignados;
    protected Aerolinea aerolinea;
    private int cantidadVuelos;
    protected static final int MAX_VUELOS = 10;
    protected double horasVueloAcumuladas;


    // CONSTRUCTOR
    public Empleado(String nombre, String tipoDocumento, String documento, String telefono, String email, String password,
    		double salarioBase, Date fechaContratacion, int aniosExperiencia) throws EValorNulo, EValorNegativo, EInvalidPass, EInvalidTelefono, EInvalidEmail, EInvalidDocumento, EInvalidName {
    	super(nombre, tipoDocumento, documento, telefono, email, password);
    	if(salarioBase<=0 || aniosExperiencia<=0)
    		throw new EValorNegativo("El salario base debe ser mayor a cero");
    	if(aniosExperiencia<0)
    		throw new EValorNegativo("Años de experiencia no puede ser negativo");
    	this.salarioBase = salarioBase;
    	this.fechaContratacion = fechaContratacion;
    	this.activo = true;
    	this.aniosExperiencia = aniosExperiencia;
    	this.vuelosAsignados = new Vuelo[MAX_VUELOS];
    	this.setCantidadVuelos(0);
    	this.horasVueloAcumuladas = 0.0;
    	this.id=IDAsign.asignar("EM",cont);
    }

    // METODOS
    public abstract double calcularSalario();

    // Metodo para registrar horas de vuelo
    public void registrarHorasVuelo(double horas) throws EValorNegativo {
        if (horas > 0) {
            this.horasVueloAcumuladas += horas;
        } else {
            throw new EValorNegativo("Las horas de vuelo deben ser mayores que cero");
        }
    }

    // Metodo para asignar vuelos al Empleado
    public void asignarVuelo(Vuelo vuelo)
            throws ECapacidadVuelosLlena, EVueloYaAsignado, EValorNulo {
        if (vuelo == null) {
            throw new EValorNulo("El vuelo no puede ser null");
        }

        if (this.getCantidadVuelos() >= this.vuelosAsignados.length) {
            throw new ECapacidadVuelosLlena("No hay espacio para más vuelos para este empleado");
        }

        // verificar que no exista ya ese id
        for (int i = 0; i < this.getCantidadVuelos(); i++) {
            if (this.vuelosAsignados[i].getId() == vuelo.getId()) {
                throw new EVueloYaAsignado(
                        "El vuelo con id " + vuelo.getId() + " ya está asignado a este empleado");
            }
        }

        this.vuelosAsignados[this.getCantidadVuelos()] = vuelo;
        this.setCantidadVuelos(this.getCantidadVuelos() + 1);
    }

    // Metodo para buscar entre los vuelos asignados al empleado
    public Vuelo[] buscarVuelosAsignados(Vuelo vuelo)
            throws EVueloNoEncontrado, EValorNulo {
        if (vuelo == null) {
            throw new EValorNulo("El vuelo no puede ser null");
        }

        int contador = 0;

        for (int i = 0; i < this.getCantidadVuelos(); i++) {
            if (this.vuelosAsignados[i].getId() == vuelo.getId()) {
                contador++;
            }
        }

        if (contador == 0) {
            throw new EVueloNoEncontrado(
                    "Vuelo con id " + vuelo.getId() + " no encontrado entre los asignados");
        }

        Vuelo[] encontrados = new Vuelo[contador];
        int index = 0;

        for (int i = 0; i < this.getCantidadVuelos(); i++) {
            if (this.vuelosAsignados[i].getId() == vuelo.getId()) {
                encontrados[index] = this.vuelosAsignados[i];
                index++;
            }
        }

        return encontrados;
    }

    // Metodo para eliminar un vuelo asignado a un empleado
    public void eliminarVueloAsignado(Vuelo vuelo)
            throws EVueloNoEncontrado, EValorNulo {
        if (vuelo == null) {
            throw new EValorNulo("El vuelo no puede ser null");
        }

        for (int i = 0; i < this.getCantidadVuelos(); i++) {
            if (this.vuelosAsignados[i].getId() == vuelo.getId()) {

                // mover elementos hacia la izquierda
                for (int j = i; j < getCantidadVuelos() - 1; j++) {
                    this.vuelosAsignados[j] = this.vuelosAsignados[j + 1];
                }

                this.vuelosAsignados[getCantidadVuelos() - 1] = null;
                this.setCantidadVuelos(this.getCantidadVuelos() - 1);

                return;
            }
        }

        throw new EVueloNoEncontrado(
                "Vuelo con id " + vuelo.getId() + " no encontrado entre los asignados");
    }

    // Método de ejemplo para serializar un Empleado a fichero
    public void guardarEnFichero(String ruta) throws IOException, EValorNulo {
        if (ruta == null || ruta.isBlank()) {
            throw new EValorNulo("La ruta del fichero no puede ser nula ni vacía");
        }

        FileOutputStream fos = new FileOutputStream(ruta);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();
    }


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

    public double getHorasVueloAcumuladas() {
        return this.horasVueloAcumuladas;
    }


    public void setSalarioBase(double salarioBase) throws EValorNegativo {
        if (salarioBase > 0) {
        	this.salarioBase = salarioBase;
        } else {
            throw new EValorNegativo("Las horas de vuelo acumuladas no pueden ser negativas");
        }
        
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setAniosExperiencia(int aniosExperiencia) throws EValorNegativo {
    	if (aniosExperiencia>= 0) {
        	this.aniosExperiencia = aniosExperiencia;
        } else {
            throw new EValorNegativo("Las horas de vuelo acumuladas no pueden ser negativas");
        }
    }

    public void setHorasVueloAcumuladas(double horasVueloAcumuladas) throws EValorNegativo {
    	if (horasVueloAcumuladas >= 0) {
            this.horasVueloAcumuladas = horasVueloAcumuladas;
        } else {
            throw new EValorNegativo("Las horas de vuelo acumuladas no pueden ser negativas");
        }
    }
	
	public Vuelo[] getVuelosAsignados() {
	    return vuelosAsignados;
	}

	public int getCantidadVuelos() {
	    return cantidadVuelos;
	}
	
	public void setCantidadVuelos(int cantidad) {
		this.cantidadVuelos = cantidad;
	}
}
