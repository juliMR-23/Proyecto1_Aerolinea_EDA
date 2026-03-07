package principal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Date;

import excepciones.EPersonaInvalida;
import excepciones.EValorNegativo;
import excepciones.EParametroInvalido;
import excepciones.ECapacidadVuelosLlena;
import excepciones.EVueloYaAsignado;
import excepciones.EVueloNoEncontrado;

abstract class Empleado extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    // ATRIBUTOS
    protected double salarioBase;
    protected Date fechaContratacion;
    protected boolean activo;
    protected int aniosExperiencia;
    protected Vuelo[] vuelosAsignados;
    protected Aerolinea aerolinea;
    protected int cantidadVuelos;
    protected static final int MAX_VUELOS = 10;
    protected double horasVueloAcumuladas;

    // CONSTRUCTOR
    public Empleado(String id, String nombre, String tipoDocumento, String documento, String telefono, String email, double salarioBase, Date fechaContratacion, boolean activo, int aniosExperiencia, Aerolinea aerolinea) throws EPersonaInvalida {
    	super(id, nombre, tipoDocumento, documento, telefono, email);
    	this.salarioBase = salarioBase;
    	this.fechaContratacion = fechaContratacion;
    	this.activo = activo;
    	this.aniosExperiencia = aniosExperiencia;
    	this.aerolinea = aerolinea;
    	this.vuelosAsignados = new Vuelo[MAX_VUELOS];
    	this.cantidadVuelos = 0;
    	this.horasVueloAcumuladas = 0.0;
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
            throws ECapacidadVuelosLlena, EVueloYaAsignado, EParametroInvalido {
        if (vuelo == null) {
            throw new EParametroInvalido("El vuelo no puede ser null");
        }

        if (this.cantidadVuelos >= this.vuelosAsignados.length) {
            throw new ECapacidadVuelosLlena("No hay espacio para más vuelos para este empleado");
        }

        // verificar que no exista ya ese id
        for (int i = 0; i < this.cantidadVuelos; i++) {
            if (this.vuelosAsignados[i].getId() == vuelo.getId()) {
                throw new EVueloYaAsignado(
                        "El vuelo con id " + vuelo.getId() + " ya está asignado a este empleado");
            }
        }

        this.vuelosAsignados[this.cantidadVuelos] = vuelo;
        this.cantidadVuelos++;
    }

    // Metodo para buscar entre los vuelos asignados al empleado
    public Vuelo[] buscarVuelosAsignados(Vuelo vuelo)
            throws EVueloNoEncontrado, EParametroInvalido {
        if (vuelo == null) {
            throw new EParametroInvalido("El vuelo no puede ser null");
        }

        int contador = 0;

        for (int i = 0; i < this.cantidadVuelos; i++) {
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

        for (int i = 0; i < this.cantidadVuelos; i++) {
            if (this.vuelosAsignados[i].getId() == vuelo.getId()) {
                encontrados[index] = this.vuelosAsignados[i];
                index++;
            }
        }

        return encontrados;
    }

    // Metodo para eliminar un vuelo asignado a un empleado
    public void eliminarVueloAsignado(Vuelo vuelo)
            throws EVueloNoEncontrado, EParametroInvalido {
        if (vuelo == null) {
            throw new EParametroInvalido("El vuelo no puede ser null");
        }

        for (int i = 0; i < this.cantidadVuelos; i++) {
            if (this.vuelosAsignados[i].getId() == vuelo.getId()) {

                // mover elementos hacia la izquierda
                for (int j = i; j < cantidadVuelos - 1; j++) {
                    this.vuelosAsignados[j] = this.vuelosAsignados[j + 1];
                }

                this.vuelosAsignados[cantidadVuelos - 1] = null;
                this.cantidadVuelos--;

                return;
            }
        }

        throw new EVueloNoEncontrado(
                "Vuelo con id " + vuelo.getId() + " no encontrado entre los asignados");
    }

    // Método de ejemplo para serializar un Empleado a fichero
    public void guardarEnFichero(String ruta) throws IOException, EParametroInvalido {
        if (ruta == null || ruta.isEmpty()) {
            throw new EParametroInvalido("La ruta del fichero no puede ser nula ni vacía");
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


    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    public void setHorasVueloAcumuladas(double horasVueloAcumuladas) throws EValorNegativo {
        if (horasVueloAcumuladas >= 0) {
            this.horasVueloAcumuladas = horasVueloAcumuladas;
        } else {
            throw new EValorNegativo("Las horas de vuelo acumuladas no pueden ser negativas");
        }
    }
}
